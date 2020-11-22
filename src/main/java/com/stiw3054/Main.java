package com.stiw3054;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) {
        try {
            String url = "https://github.com/STIW3054-A201/Main-Data/issues/1";
            String url2 = "https://github.com/STIW3054-A201/Main-Data/wiki/List_of_Student";
            String url3 = "https://github.com/STIW3054-A201/Main-Data/issues/4";
            Second page2 = new Second();
            String data1 = applyJsoup(url);
            String data2 = applyJsoup(url2);
            String data3 = getDivContent(data1);
            String data4 = tableData(data2);
            tableDataList(data4);
            githubLink(data3);
            studentName(data3);
            studentMatric(data3);
            compareData(tableDataList(data4), studentMatric(data3));
            System.out.println();
            System.out.println();
            System.out.println(" Students GitHub account.");
            System.out.println("| No. | Matric | Name                                  | GitHub Link                            |");
            System.out.println("|-----|--------|---------------------------------------|----------------------------------------|");
            for (int i = 0; i < studentMatric(data3).size(); i++) {
                int last = studentName(data3).get(i).indexOf("<br>");
                System.out.printf("|%-5s|%-8s|%-39s|%-40s|\n", i + 1, studentMatric(data3).get(i), studentName(data3).get(i).substring(4, last), githubLink(data3).get(i));
            }
            System.out.println("|-----|--------|---------------------------------------|----------------------------------------|");
            System.out.println("\n");
            page2.compareData2();
            generateExcel(data3);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static String applyJsoup(String url) {
        String content = "";
        try {
            Document doc = Jsoup.connect(url)
                    .data("jquery", "java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(50000)
                    .get();
            content = doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public static String getDivContent(String content) {
        String divContent = "";
        Document doc = Jsoup.parse(content);
        Elements divs = doc.getElementsByClass("js-timeline-item js-timeline-progressive-focus-container");
        divContent = divs.toString();
        return divContent;
    }

    public static List<String> githubLink(String divContent) {
        String abs = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        Document doc = Jsoup.parse(divContent, abs);
        List<String> linkList = new ArrayList<String>();
        Elements linkStrs = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");

        for (Element linkStr : linkStrs) {
            String url = linkStr.getElementsByTag("a").attr("abs:href");

            linkList.add(url);
        }
        return linkList;
    }

    public static List<String> studentName(String divContent) {
        String abs = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        String name = "";
        List<String> nameList = new ArrayList<String>();
        Document doc = Jsoup.parse(divContent, abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");

        for (Element matricNum : matricNums) {
            Elements url = matricNum.getElementsByTag("p");
            String transfer = url.select("p").toString();
            Pattern pattern = Pattern.compile("Name: (.*?)<br>|Name :(.*?)<br>|:\\\\s(U.*)<br>|Name:(.*?)<br>|name :( .*?)<br> |Name (.*?)<br>");// 匹配的模式 //"<p>(.*?)<br>" //:(.*)<br> //"Name: (.*?)<br>|Name :(.*?)<br>|:\\s(U.*)<br>|Name:(.*?)<br>|name :( .*?)<br> |Name (.*?)<br>"
            Matcher m = pattern.matcher(transfer);
            while (m.find()) {
                name = m.group(0);
            }

            nameList.add(name);
        }
        return nameList;
    }

    public static List<String> studentMatric(String divContent) throws FileNotFoundException, IOException {
        String abs = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        String matric = "";
        List<String> matricList = new ArrayList<String>();
        Document doc = Jsoup.parse(divContent, abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");

        for (Element matricNum : matricNums) {
            Elements url = matricNum.getElementsByTag("p");
            String transfer = url.select("p").toString();
            Pattern pattern = Pattern.compile("(\\b2.*?)<br>");
            Matcher m = pattern.matcher(transfer);
            while (m.find()) {
                matric = m.group(1);
            }
            matricList.add(matric);
        }
        return matricList;
    }

    public static String tableData(String content) {
        String divContent = "";
        Document doc = Jsoup.parse(content);
        Elements divs = doc.getElementsByClass("markdown-body");
        divContent = divs.toString();
        return divContent;
    }

    public static List<String> tableDataList(String divContent) throws IOException {
        String matric = "";
        List<String> matricList = new ArrayList<String>();

        String url = "https://github.com/STIW3054-A201/Main-Data/wiki/List_of_Student";
        Document doc = Jsoup.parse(divContent, url);

        Element element = doc.select("table").first();
        Elements els = element.select("tr");
        for (Element el : els) {
            Elements ele = el.select("td");
            String context = ele.select("td").toString();
            Pattern pattern = Pattern.compile("\\d{6}");
            Matcher m = pattern.matcher(context);
            while (m.find()) {
                matric = m.group(0);
            }
            matricList.add(matric);

        }
        return matricList;
    }

    public static void compareData(List<String> list1, List<String> list2) {
        for (String str1 : list1) {
            if (!list2.contains(str1)) {

                System.out.println(str1+ " has not comment GitHub account.");
            }
        }
        for (String str2 : list2) {
            if (list1.contains(str2)) {

                System.out.println(str2+ " has comment GitHub account.");
            }
        }
    }
    public static void generateExcel(String divContent) throws IOException {
        List<String> matricList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        List<String> linkList = new ArrayList<String>();
        String abs = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        Document doc = Jsoup.parse(divContent, abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("Page_1");

        HSSFRow row = sheet.createRow((int) 0);

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("No.");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("Matric");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("Name");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("Link");
        cell.setCellStyle(style);
        matricList.addAll(studentMatric(divContent));
        nameList.addAll(studentName(divContent));
        linkList.addAll(githubLink(divContent));
        for (int i = 0; i < matricNums.size(); i++) {
            row = sheet.createRow((int) i + 1);
            String id = Integer.toString(i + 1);
            row.createCell((short) 0).setCellValue(new HSSFRichTextString(id));
            row.createCell((short) 1).setCellValue(new HSSFRichTextString(matricList.get(i)));
            row.createCell((short) 2).setCellValue(new HSSFRichTextString(nameList.get(i)));
            row.createCell((short) 3).setCellValue(new HSSFRichTextString(linkList.get(i)));
        }
        FileOutputStream fout = new FileOutputStream("D:/Report_excel.xls");
        wb.write(fout);
        fout.close();
        System.out.println("Report has been done.");
    }
}
