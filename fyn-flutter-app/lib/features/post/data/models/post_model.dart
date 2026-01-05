import '../../../../core/utils/date_utils.dart';
import '../../../auth/data/models/user_response.dart';
import '../../../auth/data/models/profile_response.dart';
import 'post_media.dart';
import 'post_visibility.dart';
import 'location_info.dart';
import 'place_info.dart';

class PostModel {
  final String id;
  final UserResponse author;
  final String content;
  final PostVisibility visibility;
  final int likeCount;
  final int commentCount;
  final DateTime? createdAt;
  final List<PostMedia> media;
  final bool likedByCurrentUser;
  final LocationInfo? location;
  final PlaceInfo? place;

  PostModel({
    required this.id,
    required this.author,
    required this.content,
    required this.visibility,
    required this.likeCount,
    required this.commentCount,
    required this.createdAt,
    required this.media,
    required this.likedByCurrentUser,
    this.location,
    this.place,
  });

  factory PostModel.fromJson(Map<String, dynamic> json) {
    // Handle both 'author' object and 'authorId' string
    UserResponse author;
    if (json['author'] != null && json['author'] is Map<String, dynamic>) {
      author = UserResponse.fromJson(json['author'] as Map<String, dynamic>);
    } else {
      // Fallback: create a minimal author from authorId
      final authorId = json['authorId']?.toString() ?? '';
      author = UserResponse(
        id: authorId,
        username: 'User',
        email: '',
        role: 'USER',
        profile: ProfileResponse(isPrivate: false),
      );
    }
    
    return PostModel(
      id: json['id']?.toString() ?? '',
      author: author,
      content: json['content'] as String? ?? '',
      visibility: PostVisibility.fromServerValue(json['visibility'] as String?),
      likeCount: (json['likeCount'] as num?)?.toInt() ?? 0,
      commentCount: (json['commentCount'] as num?)?.toInt() ?? 0,
      likedByCurrentUser: json['likedByCurrentUser'] as bool? ?? false,
      createdAt: DateUtils.parseIso8601(json['createdAt'] as String?),
      media: (json['media'] as List<dynamic>? ?? json['mediaUrls'] as List<dynamic>? ?? [])
          .map((item) {
            if (item is String) {
              return PostMedia(mediaUrl: item, mediaType: PostMediaType.image);
            }
            return PostMedia.fromJson(item as Map<String, dynamic>);
          })
          .toList(),
      location: json['location'] != null
          ? LocationInfo.fromJson(json['location'] as Map<String, dynamic>)
          : null,
      place: json['place'] != null
          ? PlaceInfo.fromJson(json['place'] as Map<String, dynamic>)
          : null,
    );
  }

  PostModel copyWith({
    int? likeCount,
    int? commentCount,
    bool? likedByCurrentUser,
  }) {
    return PostModel(
      id: id,
      author: author,
      content: content,
      visibility: visibility,
      likeCount: likeCount ?? this.likeCount,
      commentCount: commentCount ?? this.commentCount,
      createdAt: createdAt,
      media: media,
      likedByCurrentUser: likedByCurrentUser ?? this.likedByCurrentUser,
      location: location,
      place: place,
    );
  }
}

