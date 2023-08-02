package algonquin.cst2335.cst2335_finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CcyListItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    long id;
    @ColumnInfo(name = "FromCurrencyUnit")
    String fromCcyUnit;
    @ColumnInfo(name = "FromAmount")
    double fromCcyAmt;
    @ColumnInfo(name = "ToCurrencyUnit")
    String convertedCcyUnit;
    @ColumnInfo(name = "ConvertedAmount")
    double convertedCcyAmt;
    @ColumnInfo(name = "ConvertTime")
    String timeConverted;


    public CcyListItem(){
    };

    public CcyListItem(String fromCcyUnit, double fromCcyAmt, String convertedCcyUnit, double convertedCcyAmt, String time){
        this.fromCcyUnit = fromCcyUnit;
        this.fromCcyAmt = fromCcyAmt;
        this.convertedCcyUnit = convertedCcyUnit;
        this.convertedCcyAmt = convertedCcyAmt;
        this.timeConverted = time;
    }

    public String getFromCcyUnit(){
        return fromCcyUnit;
    }

    public void setFromCcyUnit(String fromUnit){
        this.fromCcyUnit = fromUnit;
    }

    public double getFromCcyAmt(){
        return fromCcyAmt;
    }

    public void setFromCcyAmt(double amt){
        this.fromCcyAmt = amt;
    }

    public String getConvertedCcyUnit(){
        return convertedCcyUnit;
    }

    public void setConvertedCcyUnit(String toUnit){
        this.convertedCcyUnit = toUnit;
    }

    public double getConvertedCcyAmt(){
        return convertedCcyAmt;
    }

    public void setConvertedCcyAmt(double newAmt){
        this.convertedCcyAmt = newAmt;
    }

    public String getTimeConverted(){
        return timeConverted;
    }

    public void setTimeConverted(String update){
        this.timeConverted = update;
    }
}
