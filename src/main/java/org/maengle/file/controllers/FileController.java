package org.maengle.file.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileDeleteService;
import org.maengle.file.services.FileDownloadService;
import org.maengle.file.services.FileInfoService;
import org.maengle.file.services.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/file", "/file"})
public class FileController {

	private final FileUploadService fileUploadService;
	private final FileDownloadService fileDownloadService;
	private final FileInfoService fileInfoService;
	private final FileDeleteService fileDeleteService;

	@PostMapping("/upload")
	public List<FileInfo> upload(RequestUpload form, @RequestPart("file")MultipartFile[] files) {
		form.setFiles(files);
		List<FileInfo> items = fileUploadService.uploadProcess(form);

		return items;
	}

	// 파일 리스트 조회 처리
	@GetMapping({"/list/{gid}", "/list/{gid}/{location}"})
	public List<FileInfo> list(@PathVariable("gid") String gid, @PathVariable(name="location", required = false) String location) {
		List<FileInfo> items = fileInfoService.getList(gid, location);

		return items;
	}

	// 파일 한개 조회 처리
	@GetMapping("/info/{seq}")
	public FileInfo info(Long seq) {
		FileInfo item = fileInfoService.get(seq);

		return item;
	}

	// 파일 1개 삭제 처리
	@DeleteMapping("/delete/{seq}")
	public FileInfo delete(@PathVariable("seq") Long seq) {
		FileInfo item = fileDeleteService.deleteProcess(seq);

		return item;
	}

	// gid를 활용해 파일 리스트 삭제 처리
	@DeleteMapping({"/deletes/{gid}","/deletes/{gid}/{location}"})
	public List<FileInfo> deletes(@PathVariable("gid") String gid, @PathVariable(name="location", required = false) String location) {
		List<FileInfo> items = fileDeleteService.process(gid, location);

		return items;

	}




	@GetMapping("/download/{seq}")
	public void download(@PathVariable Long seq) {
		fileDownloadService.process(seq);
	}



}
