package pl.rejsykoalicja.charter.mappers;

import pl.rejsykoalicja.charter.dto.VoucherDto;
import pl.rejsykoalicja.charter.repository.entities.Voucher;

public class VoucherMapper {

    public static VoucherDto toDto(Voucher voucher) {
        return VoucherDto.builder()
                         .amount(voucher.getAmount())
                         .name(voucher.getName())
                         .tag(voucher.getTag())
                         .build();
    }
}
