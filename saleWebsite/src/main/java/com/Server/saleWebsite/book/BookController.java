package com.Server.saleWebsite.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Tag(name = "Book Management", description = "Quản lý sách")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Tạo mới sách")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sách đã được tạo"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PostMapping("/save")
    public BookDTO saveBook(@RequestBody BookDTO bookDTO) {
        return bookService.saveBook(bookDTO);
    }

    @Operation(summary = "Lấy danh sách tất cả sách")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách sách đã được trả về")
    })
    @GetMapping("/findAll")
    public List<BookDTO> findAllBooks() {
        return bookService.findAllBooks();
    }

    @Operation(summary = "Tìm sách theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thông tin sách đã được trả về"),
            @ApiResponse(responseCode = "404", description = "Sách không tồn tại")
    })
    @Parameter(name = "id", description = "ID của sách cần tìm", required = true)
    @GetMapping("/findById")
    public BookDTO findBookById(@RequestParam Long id) {
        return bookService.findBookById(id);
    }

    @Operation(summary = "Cập nhật thông tin sách")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thông tin sách đã được cập nhật"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PutMapping("/update")
    public BookDTO updateBook(@RequestBody BookDTO bookDTO) {
        return bookService.updateBook(bookDTO);
    }

    @Operation(summary = "Xóa sách theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sách đã được xóa"),
            @ApiResponse(responseCode = "404", description = "Sách không tồn tại")
    })
    @Parameter(name = "id", description = "ID của sách cần xóa", required = true)
    @DeleteMapping("/deleteById")
    public void deleteBook(@RequestParam Long id) {
        bookService.deleteBook(id);
    }
}
