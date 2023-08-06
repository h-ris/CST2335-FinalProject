package algonquin.cst2335.cst2335_finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FlightModel {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "flightId")
    public long flightId;

    @ColumnInfo(name="departureCode")
    protected String airportCode;

    @ColumnInfo(name="status")
    protected String status;

    @ColumnInfo(name="departure")
    protected String departure;

    @ColumnInfo(name="gate")
    protected String gate;

    @ColumnInfo(name="terminal")
    protected String terminal;

    @ColumnInfo(name="destination")
    String destination;

    @ColumnInfo(name="destinationCode")
    protected String destAirportCode;

    @ColumnInfo(name="delay")
    protected Integer delay;

    public FlightModel(String airportCode, String status, String departure, String gate, String terminal, String destination, String destAirportCode, int delay) {
        this.airportCode = airportCode;

        this.status = status;
        this.departure = departure;
        this.gate = gate;
        this.terminal = terminal;
        this.destination = destination;
        this.destAirportCode = destAirportCode;
        this.delay = delay;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestAirportCode() {
        return destAirportCode;
    }

    public void setDestAirportCode(String destAirportCode) {
        this.destAirportCode = destAirportCode;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

}
