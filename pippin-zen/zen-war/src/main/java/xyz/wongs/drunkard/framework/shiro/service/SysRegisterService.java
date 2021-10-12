package xyz.wongs.drunkard.framework.shiro.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import xyz.wongs.drunkard.common.constant.Constants;
import xyz.wongs.drunkard.common.utils.DateUtils;
import xyz.wongs.drunkard.common.utils.MessageUtils;
import xyz.wongs.drunkard.common.utils.ServletUtils;
import xyz.wongs.drunkard.common.utils.ShiroUtils;
import xyz.wongs.drunkard.framework.manager.AsyncManager;
import xyz.wongs.drunkard.framework.manager.factory.AsyncFactory;
import xyz.wongs.drunkard.war.constant.ShiroConstants;
import xyz.wongs.drunkard.war.constant.UserConstants;
import xyz.wongs.drunkard.war.core.domain.SysUser;
import xyz.wongs.drunkard.war.core.service.ISysUserService;

/**
 * 注册校验方法
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2019/10/9 - 21:34
 * @since 1.0.0
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    /**
     * 注册
     */
    public String register(SysUser user) {
        String msg = "", loginName = user.getLoginName(), password = user.getPassword();

        if (!ObjectUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            msg = "验证码错误";
        } else if (StringUtils.hasLength(loginName)) {
            msg = "用户名不能为空";
        } else if (StringUtils.hasLength(password)) {
            msg = "用户密码不能为空";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (loginName.length() < UserConstants.USERNAME_MIN_LENGTH
                || loginName.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(loginName))) {
            msg = "保存用户'" + loginName + "'失败，注册账号已存在";
        } else {
            user.setPwdUpdateDate(DateUtils.getNowDate());
            user.setUserName(loginName);
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
            boolean regFlag = userService.registerUser(user);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }
}
