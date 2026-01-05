// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'meetup_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$MeetupModelImpl _$$MeetupModelImplFromJson(Map<String, dynamic> json) =>
    _$MeetupModelImpl(
      id: json['id'] as String,
      organizer:
          UserSummary.fromJson(json['organizer'] as Map<String, dynamic>),
      title: json['title'] as String,
      description: json['description'] as String?,
      meetType: $enumDecode(_$MeetTypeEnumMap, json['meetType']),
      category: json['category'] as String?,
      location: json['location'] as String?,
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      scheduledAt: DateTime.parse(json['scheduledAt'] as String),
      expiresAt: json['expiresAt'] == null
          ? null
          : DateTime.parse(json['expiresAt'] as String),
      durationMinutes: (json['durationMinutes'] as num?)?.toInt(),
      maxParticipants: (json['maxParticipants'] as num).toInt(),
      acceptedCount: (json['acceptedCount'] as num).toInt(),
      pendingMatchCount: (json['pendingMatchCount'] as num).toInt(),
      status: $enumDecode(_$MeetupStatusEnumMap, json['status']),
      confirmationStatus:
          $enumDecode(_$ConfirmationStatusEnumMap, json['confirmationStatus']),
      distanceKm: (json['distanceKm'] as num?)?.toDouble(),
      userHasApplied: json['userHasApplied'] as bool,
      userMatchStatus:
          $enumDecodeNullable(_$MatchStatusEnumMap, json['userMatchStatus']),
      isPast: json['isPast'] as bool? ?? false,
      isExpired: json['isExpired'] as bool? ?? false,
      createdAt: json['createdAt'] == null
          ? null
          : DateTime.parse(json['createdAt'] as String),
    );

Map<String, dynamic> _$$MeetupModelImplToJson(_$MeetupModelImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'organizer': instance.organizer,
      'title': instance.title,
      'description': instance.description,
      'meetType': _$MeetTypeEnumMap[instance.meetType]!,
      'category': instance.category,
      'location': instance.location,
      'latitude': instance.latitude,
      'longitude': instance.longitude,
      'scheduledAt': instance.scheduledAt.toIso8601String(),
      'expiresAt': instance.expiresAt?.toIso8601String(),
      'durationMinutes': instance.durationMinutes,
      'maxParticipants': instance.maxParticipants,
      'acceptedCount': instance.acceptedCount,
      'pendingMatchCount': instance.pendingMatchCount,
      'status': _$MeetupStatusEnumMap[instance.status]!,
      'confirmationStatus':
          _$ConfirmationStatusEnumMap[instance.confirmationStatus]!,
      'distanceKm': instance.distanceKm,
      'userHasApplied': instance.userHasApplied,
      'userMatchStatus': _$MatchStatusEnumMap[instance.userMatchStatus],
      'isPast': instance.isPast,
      'isExpired': instance.isExpired,
      'createdAt': instance.createdAt?.toIso8601String(),
    };

const _$MeetTypeEnumMap = {
  MeetType.oneToOne: 'ONE_TO_ONE',
  MeetType.group: 'GROUP',
};

const _$MeetupStatusEnumMap = {
  MeetupStatus.open: 'OPEN',
  MeetupStatus.matched: 'MATCHED',
  MeetupStatus.waitingConfirmation: 'WAITING_CONFIRMATION',
  MeetupStatus.completed: 'COMPLETED',
  MeetupStatus.cancelled: 'CANCELLED',
  MeetupStatus.expired: 'EXPIRED',
};

const _$ConfirmationStatusEnumMap = {
  ConfirmationStatus.none: 'NONE',
  ConfirmationStatus.pending: 'PENDING',
  ConfirmationStatus.confirmed: 'CONFIRMED',
  ConfirmationStatus.disputed: 'DISPUTED',
  ConfirmationStatus.noShow: 'NO_SHOW',
};

const _$MatchStatusEnumMap = {
  MatchStatus.pending: 'PENDING',
  MatchStatus.accepted: 'ACCEPTED',
  MatchStatus.rejected: 'REJECTED',
  MatchStatus.cancelled: 'CANCELLED',
  MatchStatus.confirmed: 'CONFIRMED',
};

_$UserSummaryImpl _$$UserSummaryImplFromJson(Map<String, dynamic> json) =>
    _$UserSummaryImpl(
      id: json['id'] as String,
      username: json['username'] as String,
      fullName: json['fullName'] as String?,
      avatarUrl: json['avatarUrl'] as String?,
    );

Map<String, dynamic> _$$UserSummaryImplToJson(_$UserSummaryImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'username': instance.username,
      'fullName': instance.fullName,
      'avatarUrl': instance.avatarUrl,
    };

_$MeetupMatchModelImpl _$$MeetupMatchModelImplFromJson(
        Map<String, dynamic> json) =>
    _$MeetupMatchModelImpl(
      id: json['id'] as String,
      meetupId: json['meetupId'] as String,
      user: UserSummary.fromJson(json['user'] as Map<String, dynamic>),
      message: json['message'] as String?,
      status: $enumDecode(_$MatchStatusEnumMap, json['status']),
      conversationId: json['conversationId'] as String?,
      createdAt: json['createdAt'] == null
          ? null
          : DateTime.parse(json['createdAt'] as String),
      respondedAt: json['respondedAt'] == null
          ? null
          : DateTime.parse(json['respondedAt'] as String),
    );

Map<String, dynamic> _$$MeetupMatchModelImplToJson(
        _$MeetupMatchModelImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'meetupId': instance.meetupId,
      'user': instance.user,
      'message': instance.message,
      'status': _$MatchStatusEnumMap[instance.status]!,
      'conversationId': instance.conversationId,
      'createdAt': instance.createdAt?.toIso8601String(),
      'respondedAt': instance.respondedAt?.toIso8601String(),
    };
