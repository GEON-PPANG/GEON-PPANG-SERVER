package com.org.gunbbang.DTO;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HttpV2RequestDTO {
  String api_key;
  List<Event> events;

  public void setEvents(String userId, String event_type, UserPropertyRequestDTO userProperty) {
    if (events == null) {
      events = new ArrayList<>(); // events 리스트 초기화
    }
    Event newEvent = new Event();
    newEvent.setUser_id(userId);
    newEvent.setEvent_type(event_type);
    newEvent.setUser_properties(userProperty);
    events.add(newEvent);
  }

  @ToString
  public class Event {
    public String user_id;
    public String event_type;
    public UserPropertyRequestDTO user_properties;

    private void setUser_id(String userId) {
      user_id = userId;
    }

    private void setEvent_type(String eventType) {
      event_type = eventType;
    }

    private void setUser_properties(UserPropertyRequestDTO userProperties) {
      user_properties = userProperties;
    }
  }
}
