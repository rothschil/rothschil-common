#!/usr/bin/env bash
# Sam Abram<WCNGS@HOTMAIL.COM>
# Version V1.3.1
echo -ne "\033[0;33m"
cat<<EOF
                                  _oo0oo_
                                 088888880
                                 88" . "88
                                 (| -_- |)
                                  0\ = /0
                               ___/'---'\___
                             .' \\\\|     |// '.
                            / \\\\|||  :  |||// \\
                           /_ ||||| -:- |||||- \\
                          |   | \\\\\\  -  /// |   |
                          | \_|  ''\---/''  |_/ |
                          \  .-\__  '-'  __/-.  /
                        ___'. .'  /--.--\  '. .'___
                     ."" '<  '.___\_<|>_/___.' >'  "".
                    | | : '-  \'.;'\ _ /';.'/ - ' : | |
                    \  \ '_.   \_ __\ /__ _/   .-' /  /
                ====='-.____'.___ \_____/___.-'____.-'=====
                                  '=---='


              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                        佛祖保佑    TTT    永不宕机
EOF
echo -ne "\033[m"


# 全局设置下面的日志级别并且格式化打印机制 debug-1, info-2, warn-3, error-4, always-5
LOG_LEVEL=1

# 调试日志
function log_debug(){
  content="[DEBUG] $(date '+%Y-%m-%d %H:%M:%S') $@"
  [ $LOG_LEVEL -le 1  ] && echo -e "\033[32m"  ${content}  "\033[0m"
}
# 信息日志
function log_info(){
  content="[INFO] $(date '+%Y-%m-%d %H:%M:%S') $@"
  [ $LOG_LEVEL -le 2  ] && echo -e "\033[32m"  ${content} "\033[0m"
}
# 警告日志
function log_warn(){
  content="[WARN] $(date '+%Y-%m-%d %H:%M:%S') $@"
  [ $LOG_LEVEL -le 3  ] && echo -e "\033[33m" ${content} "\033[0m"
}
# 错误日志
function log_err(){
  content="[ERROR] $(date '+%Y-%m-%d %H:%M:%S') $@"
  [ $LOG_LEVEL -le 4  ] && echo -e "\033[31m" ${content} "\033[0m"
}
# 一直都会打印的日志
function log_always(){
   content="[ALWAYS] $(date '+%Y-%m-%d %H:%M:%S') $@"
   [ $LOG_LEVEL -le 5  ] && echo -e  "\033[32m" ${content} "\033[0m"
}

log_debug "Use as follows:"
log_debug "sh startup.sh /APP_NAME/ /PORT/ --spring.profiles.active=/prod/"

ADATE=$(date +%Y%m%d%H)

# input hostname
hostname=`hostname`
log_debug "User Name of the Current System Operation: $hostname"
JAVA=
if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    JAVA="$JAVA_HOME/bin/java"
    log_err "Found java executable in JAVA_HOME: $JAVA"
else
    log_err "Java is not installed. Please install JAVA 1.8 or upper version"
    exit 1
fi

# 判断JDK版本
log_info "Start to determine the JDK version and determine the startup parameters"
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
VERSION=${JAVA_VERSION%_*}
log_err "Current Java Versions is $VERSION"

CURRENT_DIRECTORY=$(pwd)
ENV_DIR="$CURRENT_DIRECTORY/logs"

if [ ! -d "$ENV_DIR"  ];then
   mkdir -p $ENV_DIR
fi

GC_LOG_PATH_DIC=$ENV_DIR/gc

if [ ! -d "$GC_LOG_PATH_DIC"  ];then
   mkdir -p $GC_LOG_PATH_DIC
fi

GC_LOG_PATH=$GC_LOG_PATH_DIC/gc-$ADATE.log
VERSION2="1.8.0"
function version_ge() { test "$(echo "$@" | tr " " "\n" | sort -rV | head -n 1)" == "$1"; }

TEMP_JVM=""
# 判断字符串是否相等
if [ "$VERSION" == "$VERSION2" ];then
#### JDK岸本为 1.8的操作
#    TEMP_JVM="-XX:+PrintGCDateStamps -XX:+UseParallelOldGC -XX:+PrintGCDetails -Xloggc:$GC_LOG_PATH"
    TEMP_JVM="-XX:+PrintGCDetails -Xloggc:$GC_LOG_PATH"
    log_err "Current Java Version Is $VERSION [ Equels ] $VERSION2, The JDK8 Configuration Is USED, $TEMP_JVM"
else
    if version_gt $VERSION $VERSION2; then
######## 比JDK 1.8版本高的参数
      TEMP_JVM="-Xlog:gc:$GC_LOG_PATH"
      log_err "Current Java Version $VERSION Is Greater Than $VERSION2, The JDK Configuration Is USED $TEMP_JVM"
    fi
fi

IP_NET=$(ip addr | awk '/^[0-9]+: / {}; /inet.*global/ {print gensub(/(.*)\/(.*)/, "\\1", "g", $2)}')

## JAVA_HOME="/usr/java/jdk1.8.0_05"
JAVA_OPTS=" -Xms512m -Xmx1024m -Xmn512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$CURRENT_DIRECTORY/heapdump.hprof -XX:InitiatingHeapOccupancyPercent=60 "
JAVA_OPTS="$JAVA_OPTS -verbose:gc $TEMP_JVM"
log_err "Startup script: $JAVA_OPTS"
PID_FILE=./bin/application.pid

### 判断进程是否存在，存在则 Kill 掉
if [ -e ${PID_FILE} ]; then
  log_err "restart application"
  PIDCONTENT=$(cat ${PID_FILE})
  ARR=($PIDCONTENT)
  log_info "${ARR[0]}:${ARR[1]}:${ARR[2]}:${ARR[3]}:${ARR[4]}"
  PID=${ARR[4]}
  log_err "try to kill process $PID"
  kill ${PID}
  rm ${PID_FILE}
fi

export SPRING_PROFILES_ACTIVE
#SPRING_PROFILES_ACTIVE=production
exec java $JAVA_OPTS -classpath .:./lib/* io.github.rothschil.RothschilApplication -mmccMaintain &

PID=$!
log_err ${PID} > ${PID_FILE}
