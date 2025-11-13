package io.github.rothschil.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IntfBo implements Serializable {


    private String interfaceName;

    private String address;

    private String remark;



}
