package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.security.entity.owner.Owner;

public interface StorageServiceBuilder {

  StorageService build(Owner owner);

}
