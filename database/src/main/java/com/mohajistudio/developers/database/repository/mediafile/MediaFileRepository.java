package com.mohajistudio.developers.database.repository.mediafile;

import com.mohajistudio.developers.database.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaFileRepository extends JpaRepository<MediaFile, UUID>, MediaFileCustomRepository {
}
