package tanger.med.codechallenge.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) class representing the import summary.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportSummaryDTO {

    /**
     * The number of records that failed to import.
     */
    int failedImportRecords;

    /**
     * The number of records successfully imported.
     */
    int importedRecords;

    /**
     * The total number of records processed.
     */
    int totalRecords;
}
