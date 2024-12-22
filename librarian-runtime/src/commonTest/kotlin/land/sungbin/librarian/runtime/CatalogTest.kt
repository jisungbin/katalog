// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class CatalogTest {
  @Test fun catalogWithTypography() {
    val catalog = Catalog(Typography::class)
    assertThat(catalog.type.qualifiedName).isEqualTo(Typography::class.qualifiedName)
  }
}
