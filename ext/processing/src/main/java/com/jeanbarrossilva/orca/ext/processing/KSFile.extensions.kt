/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.ext.processing

import com.google.devtools.ksp.symbol.KSFile
import java.nio.file.Files
import java.nio.file.Path
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtImportList

/** [Name]s imported within this [KSFile]. */
internal val KSFile.imports: List<String>
  get() =
    compile()
      ?.children
      ?.filterIsInstance<KtImportList>()
      ?.flatMap(KtImportList::getImports)
      ?.mapNotNull { it.importedReference?.text }
      .orEmpty()

/** Compiles this [KSFile]. */
fun KSFile.compile(): PsiFile? {
  val disposable = Disposer.newDisposable()
  return try {
    val config = CompilerConfiguration()
    val configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
    val env = KotlinCoreEnvironment.createForProduction(disposable, config, configFiles)
    val path = Path.of(filePath)
    val text = Files.readString(path)
    val virtualFile = LightVirtualFile(fileName, KotlinFileType.INSTANCE, text)
    PsiManager.getInstance(env.project).findFile(virtualFile)
  } finally {
    disposable.dispose()
  }
}
