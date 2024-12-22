// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class KSCatalogsTest {
  @Test fun catalogsName() {
    assertThat(KSCatalogs().toString())
      .isEqualTo("land.sungbin.librarian.runtime.Catalogs")
  }

  @Test fun configurationName() {
    assertThat(KSCatalogs.configuration().toString())
      .isEqualTo("land.sungbin.librarian.runtime.Catalogs.configuration")
  }
}
