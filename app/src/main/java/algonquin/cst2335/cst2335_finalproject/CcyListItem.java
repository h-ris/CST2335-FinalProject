package algonquin.cst2335.cst2335_finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class is used to store details about a currency conversion, including the original
 * currency unit, original amount, converted currency unit, converted amount, and the time
 * when the conversion was made.
 *
 * @see androidx.room.Entity
 * @see androidx.room.PrimaryKey
 * @see androidx.room.ColumnInfo
 * @version 1.0
 */
@Entity
public class CcyListItem {
    /**
     * The auto-generated primary key for the database table.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    long id;

    /**
     * The original currency unit for the conversion.
     */
    @ColumnInfo(name = "FromCurrencyUnit")
    String fromCcyUnit;

    /**
     * The original amount for the conversion.
     */
    @ColumnInfo(name = "FromAmount")
    double fromCcyAmt;

    /**
     * The converted currency unit after the conversion.
     */
    @ColumnInfo(name = "ToCurrencyUnit")
    String convertedCcyUnit;

    /**
     * The converted amount after the conversion.
     */
    @ColumnInfo(name = "ConvertedAmount")
    double convertedCcyAmt;

    /**
     * The time when the conversion was made.
     */
    @ColumnInfo(name = "ConvertTime")
    String timeConverted;

    /**
     * Default constructor for the CcyListItem class.
     */
    public CcyListItem(){
    };

    /**
     * Constructor for the CcyListItem class that initializes the currency conversion details.
     *
     * @param fromCcyUnit       The original currency unit for the conversion.
     * @param fromCcyAmt        The original amount for the conversion.
     * @param convertedCcyUnit  The converted currency unit after the conversion.
     * @param convertedCcyAmt   The converted amount after the conversion.
     * @param time              The time when the conversion was made.
     */
    public CcyListItem(String fromCcyUnit, double fromCcyAmt, String convertedCcyUnit, double convertedCcyAmt, String time){
        this.fromCcyUnit = fromCcyUnit;
        this.fromCcyAmt = fromCcyAmt;
        this.convertedCcyUnit = convertedCcyUnit;
        this.convertedCcyAmt = convertedCcyAmt;
        this.timeConverted = time;
    }

    /**
     * Gets the original currency unit for the currency conversion.
     *
     * @return The original currency unit as a String.
     */
    public String getFromCcyUnit(){
        return fromCcyUnit;
    }

    /**
     * Sets the original currency unit for the currency conversion.
     *
     * @param fromUnit The original currency unit to set.
     */
    public void setFromCcyUnit(String fromUnit){
        this.fromCcyUnit = fromUnit;
    }

    /**
     * Gets the original amount for the currency conversion.
     *
     * @return The original amount as a double value.
     */
    public double getFromCcyAmt(){
        return fromCcyAmt;
    }

    /**
     * Sets the original amount for the currency conversion.
     *
     * @param amt The original amount to set.
     */
    public void setFromCcyAmt(double amt){
        this.fromCcyAmt = amt;
    }

    /**
     * Gets the converted currency unit after the currency conversion.
     *
     * @return The converted currency unit as a String.
     */
    public String getConvertedCcyUnit(){
        return convertedCcyUnit;
    }

    /**
     * Sets the converted currency unit after the currency conversion.
     *
     * @param toUnit The converted currency unit to set.
     */
    public void setConvertedCcyUnit(String toUnit){
        this.convertedCcyUnit = toUnit;
    }

    /**
     * Gets the converted amount after the currency conversion.
     *
     * @return The converted amount as a double value.
     */
    public double getConvertedCcyAmt(){
        return convertedCcyAmt;
    }

    /**
     * Sets the converted amount after the currency conversion.
     *
     * @param newAmt The converted amount to set.
     */
    public void setConvertedCcyAmt(double newAmt){
        this.convertedCcyAmt = newAmt;
    }

    /**
     * Gets the time when the currency conversion was made.
     *
     * @return The time of the currency conversion as a String.
     */
    public String getTimeConverted(){
        return timeConverted;
    }

    /**
     * Sets the time when the currency conversion was made.
     *
     * @param update The time of the currency conversion to set.
     */
    public void setTimeConverted(String update){
        this.timeConverted = update;
    }
}
