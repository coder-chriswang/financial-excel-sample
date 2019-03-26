package net.yto.sample.ytopermissionsample;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 01482445(wangchao)
 * @version 1.0
 * @date 2019/3/25 18:08
 */
@Data
public class ExcelData implements Serializable {
    private static final long serialVersionUID = -8840433210109171869L;
    @ApiModelProperty(value = "发票种类")
    private String invoiceType;
    @ApiModelProperty(value = "单据号")
    private String invoiceNum;
    @ApiModelProperty(value = "单据日期")
    private String time;
    @ApiModelProperty(value = "客户编码")
    private String customerCode;
    @ApiModelProperty(value = "购方公司名称")
    private String buyCompanyName;
    @ApiModelProperty(value = "购方公司税号")
    private String buyCompanyNum;
    @ApiModelProperty(value = "购方公司地址")
    private String buyCompanyAddress;
    @ApiModelProperty(value = "购方公司银行账号")
    private String buyCompanyBankAccount;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "专用发票红票通知单号")
    private String redInvoice;
    @ApiModelProperty(value = "普通发票红票对应正数发票代码")
    private String redInvoiceCode;
    @ApiModelProperty(value = "普通发票红票对应正数发票号码")
    private String redInvoiceNum;
    @ApiModelProperty(value = "开票人")
    private String billMan;
    @ApiModelProperty(value = "复核人")
    private String checkMan;
    @ApiModelProperty(value = "收款人")
    private String receiptMan;
    @ApiModelProperty(value = "销方银行及帐号")
    private String destroyBankAccount;
    @ApiModelProperty(value = "销方地址电话")
    private String destroyAddress;
    @ApiModelProperty(value = "商品编码")
    private String goodsNum;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格型号")
    private String model;
    @ApiModelProperty(value = "计量单位")
    private String unit;
    @ApiModelProperty(value = "数量")
    private String num;
    @ApiModelProperty(value = "金额")
    private Double money;
    @ApiModelProperty(value = "税率")
    private String rate;
    @ApiModelProperty(value = "税额")
    private Double rateMoney;
    @ApiModelProperty(value = "折扣金额")
    private Double discountMoney;
    @ApiModelProperty(value = "折扣税额")
    private Double discountRateMoney;
}
