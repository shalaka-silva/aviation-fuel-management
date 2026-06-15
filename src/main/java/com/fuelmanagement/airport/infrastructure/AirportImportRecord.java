package com.fuelmanagement.airport.infrastructure;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "airport_import_records")
public class AirportImportRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false, length = 100)
    private String batchId;

    @Column(name = "icao_code", length = 4)
    private String icaoCode;

    @Column(name = "import_status", nullable = false, length = 20)
    private String importStatus;

    @Column(name = "source_data", nullable = false, columnDefinition = "TEXT")
    private String sourceData;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "airport_id")
    private Long airportId;

    @Column(name = "imported_at", nullable = false)
    private Instant importedAt = Instant.now();

    protected AirportImportRecord() {}

    public static AirportImportRecord success(String batchId, String icaoCode, String sourceData, Long airportId) {
        AirportImportRecord r = new AirportImportRecord();
        r.batchId = batchId;
        r.icaoCode = icaoCode;
        r.importStatus = "SUCCESS";
        r.sourceData = sourceData;
        r.airportId = airportId;
        return r;
    }

    public static AirportImportRecord failed(String batchId, String icaoCode, String sourceData, String errorMessage) {
        AirportImportRecord r = new AirportImportRecord();
        r.batchId = batchId;
        r.icaoCode = icaoCode;
        r.importStatus = "FAILED";
        r.sourceData = sourceData;
        r.errorMessage = errorMessage;
        return r;
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public String getIcaoCode() { return icaoCode; }
    public String getImportStatus() { return importStatus; }
    public String getSourceData() { return sourceData; }
    public String getErrorMessage() { return errorMessage; }
    public Long getAirportId() { return airportId; }
    public Instant getImportedAt() { return importedAt; }
}
