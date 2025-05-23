package com.example.work_school.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;


@Entity(tableName = "expenses")
public class Expense {

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @SerializedName("amount")
    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    private String currency;

    @ColumnInfo(name = "is_synced")
    private boolean isSynced;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    private String category;

    @ColumnInfo(name = "remark")
    @SerializedName("remark")
    private String remark;

    @ColumnInfo(name = "createdBy")
    @SerializedName("createdBy")
    private String createdBy;

    @TypeConverters(DateConverter.class)
    @JsonAdapter(ISO8601DateAdapter.class)
    @ColumnInfo(name = "createdDate")
    @SerializedName("createdDate")
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

   public boolean getIsSynced(){
       return isSynced;
   }

   public void setIsSynced(boolean isSynced){
       this.isSynced = isSynced;
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

    public static class DateConverter {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }


}
