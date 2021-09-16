package my.springcloud.account.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.model.AttachFileDetail;
import my.springcloud.common.utils.TextUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class AttachFileComponent {

	private final ThreadPoolTaskExecutor executor;

	@Value("${file.upload.root-path}")
	private String fileUploadRootPath;

	@SneakyThrows
	@PostConstruct
	private void init() {
		this.createOrGetDir(this.fileUploadRootPath);
	}

	@SneakyThrows
	private Path createOrGetDir(String dir) {
		Path path = Paths.get(dir);
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}

		return path;
	}

	/**
	 * 첨부 파일 단건 저장
	 *
	 * @param path 경로
	 * @param multipartFile multipartFile
	 * @return 첨부파일 상세
	 */
	@SneakyThrows
	public AttachFileDetail save(String path, MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			return null;
		}

		Path dirPath = (TextUtils.isEmpty(path)) ? this.createOrGetDir(this.fileUploadRootPath) : this.createOrGetDir(this.fileUploadRootPath + File.separator + path);

		String originalFileName = multipartFile.getOriginalFilename();
		String orgFileName = StringUtils.cleanPath(Objects.requireNonNull(originalFileName));
		String updatedFileName = TextUtils.getRandomAlphanumeric(20) + "." + StringUtils.getFilenameExtension(orgFileName);

		String mime = multipartFile.getContentType();
		long fileSize = multipartFile.getSize();

		Files.copy(multipartFile.getInputStream(), dirPath.resolve(updatedFileName), StandardCopyOption.REPLACE_EXISTING);
		log.debug("첨부파일 저장 완료: {}, {}, {}, {}", orgFileName, updatedFileName, fileSize, dirPath.toAbsolutePath());

		return new AttachFileDetail(orgFileName, updatedFileName, mime, fileSize, dirPath.toAbsolutePath().toString());
	}

	/**
	 * 첨부 파일 복수 저장
	 *
	 * @param path 경로
	 * @param multipartFiles multipartFiles
	 * @return 첨부파일 상세
	 */
	public List<AttachFileDetail> save(String path, MultipartFile[] multipartFiles) {
		if (multipartFiles == null) {
			return new ArrayList<>();
		}

		List<CompletableFuture<AttachFileDetail>> cfs = Arrays.stream(multipartFiles)
			// .filter(m -> !m.isEmpty())
			.map(m -> CompletableFuture.supplyAsync(() -> this.save(path, m), this.executor))
			.collect(Collectors.toList());

		return cfs.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * 첨부파일 다운로드
	 *
	 * @param filePath 파일경로
	 * @param fileName 파일명
	 * @return Resource
	 */
	@SneakyThrows
	public Resource load(String filePath, String fileName) {
		Path file = this.createOrGetDir(filePath).resolve(fileName);
		Resource resource = new FileSystemResource(file);

		if (resource.exists()) {
			return resource;
		}
		else {
			throw new ResourceNotFoundException();
		}
	}

	/**
	 * 첨부파일 삭제
	 *
	 * @param filePath 파일경로
	 * @param fileName 파일명
	 * @return true or false
	 */
	@SneakyThrows
	public Boolean delete(String filePath, String fileName) {
		Path file = this.createOrGetDir(filePath).resolve(fileName);
		return FileSystemUtils.deleteRecursively(file);
	}

}
