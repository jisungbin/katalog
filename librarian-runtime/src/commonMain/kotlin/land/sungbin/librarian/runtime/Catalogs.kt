// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

// - Catalogs.typography
public object Catalogs {
  private var _configuration = CatalogConfiguration()
  public val configuration: CatalogConfiguration get() = _configuration

  public operator fun invoke(configuration: (@CatalogDsl CatalogConfiguration).() -> Unit) {
    _configuration = _configuration.apply(configuration)
  }
}
