package net.yto.sample.ytopermissionsample;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:Excel生成工具
 * @author：young
 * @time:2018/11/7 17:10
 **/
public class ExcelUtils {

    /**
     * 写excel到response流中 即导出下载excel
     * @param response
     * @param name  excel文件名
     * @param list  数据对象list
     * @param aliasList 对象字段对应excel中的列名  顺序按照list存入顺序
     * @throws Exception
     */
    public static void write(HttpServletResponse response,String name, List<?> list, List<Alias> aliasList)throws Exception {

        if(list == null || list.size() == 0){
            return;
        }


        //中文乱码问题 转换编码
        String fileName = java.net.URLEncoder.encode(name, "UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

        try(ExcelWriter writer = ExcelUtil.getBigWriter()) {
            if(aliasList != null && aliasList.size() > 0) {
                for(Alias alias : aliasList) {
                    writer.addHeaderAlias(alias.getFieldName(),alias.getExcelFieldName());
                }
            }
            style(writer);
            writer.write(list);
            writer.setColumnWidth(-1,10);
            writer.flush(response.getOutputStream());
        }  catch (Exception e) {
            throw e;
        }
    }

    private static void style(ExcelWriter writer){
        // 定义单元格背景色
        StyleSet style = writer.getStyleSet();

        Font font = writer.createFont();
        font.setFontName("微软雅黑");
        font.setColor(Font.COLOR_NORMAL);
        //第二个参数表示是否忽略头部样式
        style.setFont(font, true);
    }


    @Data
    public static class Alias{
        private String fieldName;
        private String excelFieldName;

        public Alias(String fieldName, String excelFieldName) {
            this.fieldName = fieldName;
            this.excelFieldName = excelFieldName;
        }

    }
    
    /**
	 * 获取excel列名
	 */
	public static List<Alias> getAliasListBySwaggerAnnotation(Class clazz) {
		List<Alias> list = new ArrayList<>();
		try {
			if(clazz==null) {
				return list;
			}
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
				if(apiModelProperty!=null) {
					String excelFieldName = apiModelProperty.value();
					String fieldName = field.getName();
					list.add(new Alias(fieldName, excelFieldName));
				}
			}
			Class superclass = clazz.getSuperclass();
			if(superclass!=null) {
				Field[] superDeclaredFields = superclass.getDeclaredFields();
				for (Field field : superDeclaredFields) {
					ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
					if(apiModelProperty!=null) {
						String excelFieldName = apiModelProperty.value();
						String fieldName = field.getName();
						list.add(new Alias(fieldName, excelFieldName));
					}
				}
			}
		} catch (Exception e) {}
		return list;
	}
}
