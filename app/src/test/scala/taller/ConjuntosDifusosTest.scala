package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConjuntosDifusosTest extends AnyFunSuite {

  val cd = new ConjuntosDifusos()

  // ----------------------------------------------------------
  // 🧩 Prueba 1: Conjunto difuso de números grandes
  // ----------------------------------------------------------
  test("grande debería retornar valores entre 0 y 1, y aumentar con n") {
    val g = cd.grande(2, 3)
    assert(g(1) >= 0.0 && g(1) <= 1.0)
    assert(g(10) >= g(5))
    assert(g(100) > g(10))
  }

  // ----------------------------------------------------------
  // 🧩 Prueba 2: Unión de dos conjuntos difusos
  // ----------------------------------------------------------
  test("union debería retornar el máximo entre los grados de pertenencia (conjuntos diferentes)") {
    // A = conjunto de números "cercanos a 5"
    val A: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 5)
      1.0 / (1.0 + distancia)
    }

    // B = conjunto de números "múltiplos de 3" (difuso)
    val B: cd.ConjDifuso = (x: Int) => {
      val resto = x % 3
      1.0 - (resto.toDouble / 3.0)
    }

    val unionAB = cd.union(A, B)

    // Pruebas de comportamiento esperado
    assert(unionAB(3) >= unionAB(2))  // 3 es múltiplo de 3 y está cerca de 5
    assert(unionAB(5) == 1.0)         // 5 está en el centro de A
    assert(unionAB(9) >= unionAB(7))  // 9 es múltiplo de 3
  }

  // ----------------------------------------------------------
  // 🧩 Prueba 3: Intersección de dos conjuntos difusos
  // ----------------------------------------------------------
  test("interseccion debería retornar el mínimo entre los grados de pertenencia (conjuntos diferentes)") {
    // C = conjunto de números "cercanos a 10"
    val C: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 10)
      Math.max(0.0, 1.0 - (distancia / 10.0))
    }

    // D = conjunto de números "grandes positivos"
    val D: cd.ConjDifuso = (x: Int) => {
      if (x <= 0) 0.0
      else 1.0 - 1.0 / (x.toDouble + 1.0)
    }

    val interCD = cd.interseccion(C, D)

    // Casos de prueba esperados
    assert(interCD(10) > interCD(2))  // 10 pertenece más que 2
    assert(interCD(15) > interCD(0))  // 15 está más cerca de 10 y es positivo
    assert(interCD(100) < interCD(10)) // 100 ya no está "cerca" de 10
  }
  // ----------------------------------------------------------
  // 🧩 Prueba 4: Complemento de conjuntos difusos
  // ----------------------------------------------------------
  test("complemento debería invertir los grados de pertenencia correctamente") {
    // A = conjunto de números "cercanos a 5"
    val A: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 5)
      1.0 / (1.0 + distancia)
    }

    // C = conjunto de números "cercanos a 10"
    val C: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 10)
      Math.max(0.0, 1.0 - (distancia / 10.0))
    }

    // Complementos
    val compA = cd.complemento(A)
    val compC = cd.complemento(C)

    // 🔹 Verificar propiedades básicas
    assert(math.abs(compA(5) - (1.0 - A(5))) < 0.0001) // complemento(5) ≈ 0
    assert(math.abs(compC(10) - (1.0 - C(10))) < 0.0001) // complemento(10) ≈ 0

    // 🔹 Verificar inversión de grados
    assert(compA(0) > compA(5))  // El complemento de un valor lejano a 5 debe ser mayor
    assert(compC(0) > compC(10)) // El complemento de un número lejos de 10 debe ser mayor

    // 🔹 Verificar límites de rango
    assert(compA(5) >= 0.0 && compA(5) <= 1.0)
    assert(compC(20) >= 0.0 && compC(20) <= 1.0)
  }
  // ----------------------------------------------------------
  // 🧩 Prueba 5: Inclusión entre conjuntos difusos
  // ----------------------------------------------------------
  test("inclusion debería determinar si todos los grados de un conjunto son menores o iguales a los del otro") {

    // A = conjunto de números "cercanos a 5"
    val A: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 5)
      1.0 / (1.0 + distancia)
    }

    // B = conjunto de números "múltiplos de 3" (difuso)
    val B: cd.ConjDifuso = (x: Int) => {
      val resto = x % 3
      1.0 - (resto.toDouble / 3.0)
    }

    // C = conjunto de números "cercanos a 10"
    val C: cd.ConjDifuso = (x: Int) => {
      val distancia = Math.abs(x - 10)
      Math.max(0.0, 1.0 - (distancia / 10.0))
    }

    // D = conjunto de números "grandes positivos"
    val D: cd.ConjDifuso = (x: Int) => {
      if (x <= 0) 0.0
      else 1.0 - 1.0 / (x.toDouble + 1.0)
    }

    // E = conjunto de números pequeños
    val E: cd.ConjDifuso = (x: Int) => {
      if (x <= 0) 1.0
      else 1.0 / (x + 1.0)
    }

    // Ejemplos donde la inclusión debería cumplirse
    assert(cd.inclusion(C, D))  // 1. Cercanos a 10 ⊆ grandes positivos
    assert(cd.inclusion(E, C))  // 2. Números pequeños ⊆ cercanos a 10 (bajo nivel de pertenencia)
    assert(cd.inclusion(E, D))  // 3. Pequeños ⊆ grandes positivos
    assert(cd.inclusion(C, C))  // 4.
    assert(cd.inclusion(A, D))  // 5. Números cercanos a 5 ⊆ grandes positivos

    // Ejemplos donde la inclusión NO debería cumplirse
    assert(!cd.inclusion(A, B)) // 1. Cercanos a 5 no ⊆ múltiplos de 3
    assert(!cd.inclusion(B, A)) // 2. Múltiplos de 3 no ⊆ cercanos a 5
    assert(!cd.inclusion(D, C)) // 3. Grandes no ⊆ cercanos a 10
    assert(!cd.inclusion(B, D)) // 4. Múltiplos de 3 no ⊆ grandes positivos (algunos bajos)
    assert(!cd.inclusion(A, E)) // 5. Cercanos a 5 no ⊆ pequeños
  }

  // ----------------------------------------------------------
  // 🧩 Prueba 6: Igualdad entre conjuntos difusos
  // ----------------------------------------------------------
  test("igualdad debería verificar si dos conjuntos difusos tienen los mismos grados de pertenencia") {

    // E y F son idénticos → deberían ser iguales
    val E: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 4))
    val F: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 4))

    // G y H difieren en la distancia usada → no deberían ser iguales
    val G: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 2))
    val H: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 8))

    // I y J son similares, pero una versión "más difusa" (más suave)
    val I: cd.ConjDifuso = (x: Int) => Math.max(0.0, 1.0 - (Math.abs(x - 10) / 10.0))
    val J: cd.ConjDifuso = (x: Int) => Math.max(0.0, 1.0 - (Math.abs(x - 10) / 20.0))

    // K y L tienen misma forma (función simétrica), pero en distinto centro
    val K: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 3))
    val L: cd.ConjDifuso = (x: Int) => 1.0 / (1.0 + Math.abs(x - 4))

    // M y N son ambos funciones crecientes de tipo “grande”, pero con distinto exponente
    val M = cd.grande(2, 3)
    val N = cd.grande(2, 5)

    // Casos esperados
    assert(cd.igualdad(E, F))     // 1. Idénticos
    assert(!cd.igualdad(G, H))    // 2. Distintos centros
    assert(!cd.igualdad(I, J))    // 3. Diferente grado de difusidad
    assert(!cd.igualdad(K, L))    // 4. Mismo tipo, diferente centro
    assert(!cd.igualdad(M, N))    // 5. Mismo tipo, diferente pendiente (e)
  }
}