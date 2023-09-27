package com.org.gunbbang.DTO;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HttpV2ResponseDTO {
  int code;
  int events_ingested;
  int payload_size_bytes;
  long server_upload_time;
}
