package usach.hackaton.gpu.dtos;

public record LoginResponse(
    String userId,
    String userEmail,
    String userStatus,
    String roleCode,
    String token) {
}
