package com.contact.management.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Controller
class FrontController {

    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("message", "Hello, Thymeleaf with Kotlin!")
        return "index"  // resources/templates/index.html 파일을 반환
    }

    // 파일 업로드 폼 페이지
    @GetMapping("/front/upload")
    fun showUploadForm(model: Model): String {
        return "uploadForm" // Thymeleaf 템플릿 이름
    }

    // 단일 파일 업로드 처리
    @PostMapping("/front/upload")
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String {
        if (file.isEmpty) {
            redirectAttributes.addFlashAttribute("message", "파일을 선택하세요.")
            return "redirect:/front/upload"
        }

        try {
            val targetLocation: Path = Paths.get("uploads").resolve(file.originalFilename!!)
            Files.createDirectories(targetLocation.parent)
            file.inputStream.use { inputStream ->
                Files.copy(inputStream, targetLocation)
            }
            redirectAttributes.addFlashAttribute("message", "파일 업로드 성공: ${file.originalFilename}")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패: ${e.message}")
        }

        return "redirect:/front/upload"
    }

    // 멀티파일 업로드 처리
    @PostMapping("/front/multi-upload")
    fun handleMultiFileUpload(
        @RequestParam("files") files: Array<MultipartFile>,
        redirectAttributes: RedirectAttributes
    ): String {
        if (files.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "파일을 선택하세요.")
            return "redirect:/front/upload"
        }

        try {
            files.forEach { file ->
                if (!file.isEmpty) {
                    val targetLocation: Path = Paths.get("uploads").resolve(file.originalFilename!!)
                    Files.createDirectories(targetLocation.parent)
                    file.inputStream.use { inputStream ->
                        Files.copy(inputStream, targetLocation)
                    }
                }
            }
            redirectAttributes.addFlashAttribute("message", "파일들 업로드 성공")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패: ${e.message}")
        }

        return "redirect:/front/upload"
    }

}