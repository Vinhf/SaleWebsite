package com.Server.saleWebsite.user;

import com.Server.saleWebsite.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Quản lý người dùng")
public class UserController {
	@Autowired
	private UserService service;

	@Operation(summary = "Tạo mới người dùng", description = "Lưu một người dùng mới vào hệ thống")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Người dùng đã được tạo"),
			@ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content)
	})
	@PostMapping("/save")
	public void save(@RequestBody UserDTO userDTO) {
		User user = UserMapper.mapToUser(userDTO, Role.valueOf(userDTO.getRole()));
		service.saveUser(user);
	}

	@Operation(summary = "Lấy danh sách tất cả người dùng", description = "Trả về danh sách tất cả người dùng trong hệ thống")
	@GetMapping("/findAll")
	public List<UserDTO> findAll() {
		return service.findAll();
	}

	@Operation(summary = "Lấy danh sách người dùng không phải ADMIN", description = "Trả về danh sách người dùng không có vai trò ADMIN")
	@GetMapping("/findNoADMIN")
	public List<UserDTO> findNoAMIN() {
		return service.findNoAMIN();
	}

	@Operation(summary = "Tìm người dùng theo ID", description = "Trả về thông tin người dùng theo ID")
	@Parameter(name = "user_id", description = "ID của người dùng cần tìm", required = true)
	@GetMapping("/findById")
	public UserDTO findById(@RequestParam Long user_id) {
		return service.findById(user_id);
	}

	@Operation(summary = "Tìm người dùng theo email", description = "Trả về thông tin người dùng theo email")
	@Parameter(name = "email", description = "Email của người dùng cần tìm", required = true)
	@GetMapping("/findByEmail")
	public UserDTO findByEmail(@RequestParam String email) {
		return service.findByEmail(email);
	}

	@Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật thông tin của người dùng hiện có")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Thông tin người dùng đã được cập nhật"),
			@ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content)
	})
	@PutMapping("/update")
	public UserDTO update(@RequestBody UserDTO userDTO) {
		User user = UserMapper.mapToUser(userDTO, Role.valueOf(userDTO.getRole()));
		User updatedUser = service.updateUser(user);
		return UserMapper.mapToUserDto(updatedUser);
	}

	@Operation(summary = "Xóa người dùng theo ID", description = "Xóa người dùng khỏi hệ thống theo ID")
	@Parameter(name = "user_id", description = "ID của người dùng cần xóa", required = true)
	@DeleteMapping("/deleteById")
	public void delete(@RequestParam Long user_id) {
		service.deleteUser(user_id);
	}
}
