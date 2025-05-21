package com.example.work_school.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Expense {

    private String id;
    private int amount;

    private String currency;

    private String category;

    private String remark;

    private String createdBy;

    @JsonAdapter(ISO8601DateAdapter.class)
    private Date createdDate;

   public String getId(){
       return id;
   }

   public int getAmount(){
       return amount;
   }

   public String getCurrency(){
       return currency;
   }

   public String getCategory(){
       return category;
   }

   public String getRemark(){
       return remark;
   }

   public String getCreatedBy(){
       return createdBy;
   }

   public Date getCreatedDate(){
       return createdDate;
   }

   public void setId(String id){
       this.id = id;
   }

   public void setAmount(int amount){
       this.amount = amount;
   }

   public void setCurrency(String currency){
       this.currency = currency;
   }

   public void setCreatedBy(String createdBy){
       this.createdBy = createdBy;
   }

   public void setCreatedDate(Date createdDate){
       this.createdDate = createdDate;
   }

   public Expense(int amount,String currency,String category,String remark,String createdBy,Date createdDate){
       this.id = UUID.randomUUID().toString();
       this.amount = amount;
       this.currency = currency;
       this.category = category;
       this.remark = remark;
       this.createdBy = createdBy;
       this.createdDate = createdDate;
   }

    public static class ISO8601DateAdapter extends TypeAdapter<Date> {
        private final SimpleDateFormat formatter;

        public ISO8601DateAdapter() {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public void write(JsonWriter out, Date date) throws IOException {
            if (date == null) {
                out.nullValue();
            } else {
                out.value(formatter.format(date));
            }
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            try {
                return formatter.parse(in.nextString());
            } catch (Exception e) {
                return null;
            }
        }
    }




}
