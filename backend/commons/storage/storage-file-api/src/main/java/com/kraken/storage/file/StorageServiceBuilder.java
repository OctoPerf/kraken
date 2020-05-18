package com.kraken.storage.file;

import com.kraken.security.entity.owner.Owner;
import com.kraken.storage.entity.StorageNode;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

public interface StorageServiceBuilder {

  StorageService build(Owner owner);

}
