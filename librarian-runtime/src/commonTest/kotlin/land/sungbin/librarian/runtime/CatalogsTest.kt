// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import kotlin.test.Test

class CatalogsTest {
  @Test fun catalogsDefaultConfiguration() {
    assertThat(Catalogs.configuration).isDataClassEqualTo(CatalogConfiguration())
  }

  @Test fun catalogsConfigurationModificationViaDsl() {
    val text = "Hello, Kotlin!"

    Catalogs {
      typographyPreviewText = text
    }

    assertThat(Catalogs.configuration)
      .isDataClassEqualTo(CatalogConfiguration(typographyPreviewText = text))
  }
}
