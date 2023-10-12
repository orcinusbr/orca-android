package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.instance.domain.Domain
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class DomainTests {
  @Test
  fun `GIVEN a blank domain WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.BlankValueException> { Domain(" ") }
  }

  @Test
  fun `GIVEN an domain with an illegal character WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.IllegalValueException> { Domain("@appleseed.com") }
  }

  @Test
  fun `GIVEN an domain without a TLD WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.ValueWithoutTopLevelDomainException> { Domain("appleseed.") }
  }
}
