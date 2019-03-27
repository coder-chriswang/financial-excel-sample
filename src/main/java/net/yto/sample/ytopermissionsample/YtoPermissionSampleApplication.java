package net.yto.sample.ytopermissionsample;

import cn.hutool.core.bean.BeanUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
//@EnableOAuth2Sso
//@EnableResourceService
public class YtoPermissionSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtoPermissionSampleApplication.class, args);
	}

    @Controller
    static class HomeController {
        @Autowired
        private Environment env;
        private static final String SUFFIX_2003 = ".xls";
        private static final String SUFFIX_2007 = ".xlsx";


        @RequestMapping("/wangchao")
        String home(Model model) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            Object detail = authentication.getDetails();
//            Object principle = authentication.getPrincipal();
//            Object authority = authentication.getAuthorities();
//            model.addAttribute("detail",detail);
//            model.addAttribute("principle",principle);
//            model.addAttribute("authority",authority);
//            model.addAttribute("authServer",env.getProperty("auth-server"));
//            // 部门岗位信息
//            System.out.println("authentication details is : " + detail);
//            // principle
//            System.out.println("authentication principle is : " + principle);
//            // authority
//            System.out.println("authentication authority is : " + authority);
            return "index";
        }

        @RequestMapping("/wangchao/hello")
        @ResponseBody
        String hello() {
            return "hello wangchao!";
        }

        @RequestMapping("/wangchao/index")
        String origin() {
            return "upload";
        }

        @RequestMapping(value = "/uploadAndDownload", method = RequestMethod.POST)
        @ResponseBody
        public void uploadExcel(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
            List<ExcelData> result = new ArrayList<>();
            try {
                // 获取数据列表
                List<ExcelData> excelDataList = resolveExcel(file);
                for (ExcelData excelData : excelDataList) {
                    Double money = excelData.getMoney();
                    if (money > 100000) {
                        double i = Math.rint(money/100000);
                        int j = (int) i;
                        for (int temp = 1; temp <= j; temp++) {
                            ExcelData t = new ExcelData();
                            BeanUtil.copyProperties(excelData,t);
                            t.setMoney(100000.00);
                            result.add(t);
                        }
                        Double leftMoney = money - j*100000;
                        ExcelData e = new ExcelData();
                        BeanUtil.copyProperties(excelData,e);
                        e.setMoney(leftMoney);
                        result.add(e);
                    } else {
                        result.add(excelData);
                    }
                }
                long timeString = System.currentTimeMillis();
                ExcelUtils.write(response,"财务拆分数据"+timeString,result,ExcelUtils.getAliasListBySwaggerAnnotation(ExcelData.class));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<ExcelData> resolveExcel(MultipartFile file) throws Exception {
            if (file == null) {
                throw new Exception("文件对象为空");
            }
            List<ExcelData> excelDataList = new ArrayList<>();
            // 校验
            //获取文件的名字
            String originalFilename = file.getOriginalFilename();
            Workbook workbook = null;
            try {
                if (originalFilename.endsWith(SUFFIX_2003)) {
                    workbook = new HSSFWorkbook(file.getInputStream());
                } else if (originalFilename.endsWith(SUFFIX_2007)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("格式错误");
            }
            if (workbook == null) {
                throw new Exception("格式错误");
            }
            else {
                // 解析excel
                Sheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    ExcelData excelData = new ExcelData();
                    // 获取发票种类
                    if (row.getCell(0) != null) {
                        row.getCell(0).setCellType(CellType.STRING);
                        String invoiceType = row.getCell(0).getStringCellValue();
                        excelData.setInvoiceType(invoiceType);
                    }
                    // 单据号
                    if (row.getCell(1) != null) {
                        row.getCell(1).setCellType(CellType.STRING);
                        String invoiceNum = row.getCell(1).getStringCellValue();
                        excelData.setInvoiceNum(invoiceNum);
                    }
                    // 单据日期
                    if (row.getCell(2) != null) {
                        row.getCell(2).setCellType(CellType.STRING);
                        String time = row.getCell(2).getStringCellValue();
                        excelData.setTime(time);
                    }
                    // 客户编码
                    if (row.getCell(3) != null) {
                        row.getCell(3).setCellType(CellType.STRING);
                        String customerCode = row.getCell(3).getStringCellValue();
                        excelData.setCustomerCode(customerCode);
                    }
                    // 购方公司名称
                    if (row.getCell(4) != null) {
                        row.getCell(4).setCellType(CellType.STRING);
                        String buyCompanyName = row.getCell(4).getStringCellValue();
                        excelData.setBuyCompanyName(buyCompanyName);
                    }
                    // 购方公司税号
                    if (row.getCell(5) != null) {
                        row.getCell(5).setCellType(CellType.STRING);
                        String buyCompanyNum = row.getCell(5).getStringCellValue();
                        excelData.setBuyCompanyNum(buyCompanyNum);
                    }
                    // 购方公司地址
                    if (row.getCell(6) != null) {
                        row.getCell(6).setCellType(CellType.STRING);
                        String buyCompanyAddress = row.getCell(6).getStringCellValue();
                        excelData.setBuyCompanyAddress(buyCompanyAddress);
                    }
                    // 购方公司银行账号
                    if (row.getCell(7) != null) {
                        row.getCell(7).setCellType(CellType.STRING);
                        String buyCompanyBankAccount = row.getCell(7).getStringCellValue();
                        excelData.setBuyCompanyBankAccount(buyCompanyBankAccount);
                    }
                    // 备注
                    if (row.getCell(8) != null) {
                        row.getCell(8).setCellType(CellType.STRING);
                        String remark = row.getCell(7).getStringCellValue();
                        excelData.setRemark(remark);
                    }
                    // 专用发票红票通知单号
                    if (row.getCell(9) != null) {
                        row.getCell(9).setCellType(CellType.STRING);
                        String redInvoice = row.getCell(7).getStringCellValue();
                        excelData.setRedInvoice(redInvoice);
                    }
                    // 普通发票红票对应正数发票代码
                    if (row.getCell(10) != null) {
                        row.getCell(10).setCellType(CellType.STRING);
                        String redInvoiceCode = row.getCell(10).getStringCellValue();
                        excelData.setRedInvoiceCode(redInvoiceCode);
                    }
                    // 普通发票红票对应正数发票号码
                    if (row.getCell(11) != null) {
                        row.getCell(11).setCellType(CellType.STRING);
                        String redInvoiceNum = row.getCell(11).getStringCellValue();
                        excelData.setRedInvoiceNum(redInvoiceNum);
                    }
                    // 开票人
                    if (row.getCell(12) != null) {
                        row.getCell(12).setCellType(CellType.STRING);
                        String billMan = row.getCell(12).getStringCellValue();
                        excelData.setBillMan(billMan);
                    }
                    // 复核人
                    if (row.getCell(13) != null) {
                        row.getCell(13).setCellType(CellType.STRING);
                        String checkMan = row.getCell(13).getStringCellValue();
                        excelData.setCheckMan(checkMan);
                    }
                    // 收款人
                    if (row.getCell(14) != null) {
                        row.getCell(14).setCellType(CellType.STRING);
                        String receiptMan = row.getCell(14).getStringCellValue();
                        excelData.setReceiptMan(receiptMan);
                    }
                    // 销方银行及帐号
                    if (row.getCell(15) != null) {
                        row.getCell(15).setCellType(CellType.STRING);
                        String destroyBankAccount = row.getCell(15).getStringCellValue();
                        excelData.setDestroyBankAccount(destroyBankAccount);
                    }
                    // 销方地址电话
                    if (row.getCell(16) != null) {
                        row.getCell(16).setCellType(CellType.STRING);
                        String destroyAddress = row.getCell(16).getStringCellValue();
                        excelData.setDestroyAddress(destroyAddress);
                    }
                    // 商品编码
                    if (row.getCell(17) != null) {
                        row.getCell(17).setCellType(CellType.STRING);
                        String goodsNum = row.getCell(17).getStringCellValue();
                        excelData.setGoodsNum(goodsNum);
                    }
                    // 商品名称
                    if (row.getCell(18) != null) {
                        row.getCell(18).setCellType(CellType.STRING);
                        String goodsName = row.getCell(18).getStringCellValue();
                        excelData.setGoodsName(goodsName);
                    }
                    // 规格型号
                    if (row.getCell(19) != null) {
                        row.getCell(19).setCellType(CellType.STRING);
                        String model = row.getCell(19).getStringCellValue();
                        excelData.setModel(model);
                    }
                    // 计量单位
                    if (row.getCell(20) != null) {
                        row.getCell(20).setCellType(CellType.STRING);
                        String unit = row.getCell(20).getStringCellValue();
                        excelData.setUnit(unit);
                    }
                    // 数量
                    if (row.getCell(21) != null) {
                        row.getCell(21).setCellType(CellType.STRING);
                        String num = row.getCell(21).getStringCellValue();
                        excelData.setNum(num);
                    }
                    // 金额
                    if (row.getCell(22) != null) {
                        row.getCell(22).setCellType(CellType.NUMERIC);
                        Double money = row.getCell(22).getNumericCellValue();
                        excelData.setMoney(money);
                    }
                    // 税率
                    if (row.getCell(23) != null) {
                        row.getCell(23).setCellType(CellType.STRING);
                        String rate = row.getCell(23).getStringCellValue();
                        excelData.setRate(rate);
                    }
                    // 税额
                    if (row.getCell(24) != null) {
                        row.getCell(24).setCellType(CellType.NUMERIC);
                        Double rateMoney = row.getCell(24).getNumericCellValue();
                        excelData.setRateMoney(rateMoney);
                    }
                    // 折扣金额
                    if (row.getCell(25) != null) {
                        row.getCell(25).setCellType(CellType.NUMERIC);
                        Double discountMoney = row.getCell(25).getNumericCellValue();
                        excelData.setDiscountMoney(discountMoney);
                    }
                    // 折扣金额
                    if (row.getCell(26) != null) {
                        row.getCell(26).setCellType(CellType.NUMERIC);
                        Double discountRateMoney = row.getCell(26).getNumericCellValue();
                        excelData.setDiscountRateMoney(discountRateMoney);
                    }
                    excelDataList.add(excelData);
                }
            }
            return excelDataList;
        }
    }
}
