import 'package:json_annotation/json_annotation.dart';
import 'user_response.dart';
import 'profile_response.dart';

part 'auth_response.g.dart';

@JsonSerializable()
class AuthResponse {
  final String? accessToken;
  final String? refreshToken;
  final int? expiresIn;
  final UserResponse user;

  AuthResponse({
    this.accessToken,
    this.refreshToken,
    this.expiresIn,
    required this.user,
  });

  /// Custom fromJson to handle backend response format
  /// Backend returns: {token, username, userId, role}
  /// We need to create UserResponse from these flat fields
  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    // Check if backend returns flat format (token instead of accessToken)
    if (json.containsKey('token') && !json.containsKey('user')) {
      // Create UserResponse from flat fields
      final user = UserResponse(
        id: json['userId'] as String? ?? '',
        username: json['username'] as String? ?? '',
        role: json['role'] as String?,
        profile: ProfileResponse(isPrivate: false),
      );
      
      return AuthResponse(
        accessToken: json['token'] as String?,
        refreshToken: json['refreshToken'] as String?,
        expiresIn: json['expiresIn'] as int?,
        user: user,
      );
    }
    
    // Original format with nested user object
    return _$AuthResponseFromJson(json);
  }

  Map<String, dynamic> toJson() => _$AuthResponseToJson(this);
}
