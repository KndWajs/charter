package pl.rejsykoalicja.charter.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.service.CharterService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/charter")
@AllArgsConstructor
public class CharterController {

    private final CharterService charterService;

    @GetMapping(value = "/all")
    public List<CharterDto> getAll() {
        return charterService.getAll();
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CharterDto save(@RequestBody CharterDto dto) {
        return charterService.save(dto);
    }

    @PutMapping(value = "/create-charter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CharterDto create(@RequestBody CharterDto dto) {
        return charterService.createCharter(dto);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CharterDto update(@RequestBody CharterDto dto) {
        return charterService.update(dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable BigInteger id) {
        charterService.deleteById(id);
    }

    @GetMapping(value = "/therms-and-conditions", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getThermsAndConditions
            (@RequestParam(name = "version") String version) throws IOException {
        byte[] pdf = charterService.getThermsAndConditions(version);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Therms and Conditions.pdf");
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(pdf)));
    }

    @GetMapping(value = "/agreement", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getCharterAgreement
            (@RequestParam(name = "charter-id") BigInteger charterId) throws IOException {
        byte[] pdf = charterService.getCharterAgreement(charterId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Charter agreement.pdf");
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(pdf)));
    }
}
