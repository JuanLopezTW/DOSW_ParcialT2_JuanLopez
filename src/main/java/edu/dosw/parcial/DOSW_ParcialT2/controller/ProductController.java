package edu.dosw.parcial.DOSW_ParcialT2.controller;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.ProductResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.mappers.ProductMapper;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{codQR}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductResponse> getByQrCode(@PathVariable String codQR) {
        log.info("GET /api/products/{}", codQR);
        Product product = productService.getByQrCode(codQR);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }
}
