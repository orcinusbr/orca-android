/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
