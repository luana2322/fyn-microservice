import 'package:json_annotation/json_annotation.dart';

enum MeetType {
  @JsonValue('ONE_TO_ONE')
  oneToOne('ONE_TO_ONE'),
  @JsonValue('GROUP')
  group('GROUP');

  final String value;
  const MeetType(this.value);

  static MeetType fromString(String value) {
    return MeetType.values.firstWhere(
      (e) => e.value == value,
      orElse: () => MeetType.group,
    );
  }
}

enum MeetupStatus {
  @JsonValue('OPEN')
  open('OPEN'),
  @JsonValue('MATCHED')
  matched('MATCHED'),
  @JsonValue('WAITING_CONFIRMATION')
  waitingConfirmation('WAITING_CONFIRMATION'),
  @JsonValue('COMPLETED')
  completed('COMPLETED'),
  @JsonValue('CANCELLED')
  cancelled('CANCELLED'),
  @JsonValue('EXPIRED')
  expired('EXPIRED');

  final String value;
  const MeetupStatus(this.value);

  static MeetupStatus fromString(String value) {
    return MeetupStatus.values.firstWhere(
      (e) => e.value == value,
      orElse: () => MeetupStatus.open,
    );
  }
}

enum MatchStatus {
  @JsonValue('PENDING')
  pending('PENDING'),
  @JsonValue('ACCEPTED')
  accepted('ACCEPTED'),
  @JsonValue('REJECTED')
  rejected('REJECTED'),
  @JsonValue('CANCELLED')
  cancelled('CANCELLED'),
  @JsonValue('CONFIRMED')
  confirmed('CONFIRMED');

  final String value;
  const MatchStatus(this.value);

  static MatchStatus fromString(String value) {
    return MatchStatus.values.firstWhere(
      (e) => e.value == value,
      orElse: () => MatchStatus.pending,
    );
  }
}

enum ConfirmationStatus {
  @JsonValue('NONE')
  none('NONE'),
  @JsonValue('PENDING')
  pending('PENDING'),
  @JsonValue('CONFIRMED')
  confirmed('CONFIRMED'),
  @JsonValue('DISPUTED')
  disputed('DISPUTED'),
  @JsonValue('NO_SHOW')
  noShow('NO_SHOW');

  final String value;
  const ConfirmationStatus(this.value);

  static ConfirmationStatus fromString(String value) {
    return ConfirmationStatus.values.firstWhere(
      (e) => e.value == value,
      orElse: () => ConfirmationStatus.none,
    );
  }
}
