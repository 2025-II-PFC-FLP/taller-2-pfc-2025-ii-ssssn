# Taller 2: Programación Funcional y Concurrente
## Funciones básicas sobre conjuntos difusos

---
# 1. Introducción

Este informe documenta el desarrollo del Taller 2 de *Programación Funcional y Concurrente*, en el cuál su objetivo es implementar y realizar distintas funciones básicas entre conjuntos difusos como lo son la unión o la intersección, además de determinar si un número cualquiera es grande o no, entre otras funciones presentadas a continuación.

En el desarrollo de este taller se presentan seis funciones diferentes:
1. *Grande*
2. *Complemento*
3. *Unión*
4. *Intersección*
5. *Inclusión*
6. *Igualdad*

El taller cumple con las indicaciones presentadas de programación funcional: no utilizar variables mutables ni ciclos `for` o `while`, tampoco sentencias `return` en las funciones.

---
# 2. Fundamento teórico

## 2.1. Grande
Para determinar si un número es grande, una forma es calcular $\dfrac{n}{n+d}$, donde $d$ es un número pequeño mayor o igual a $1$. En caso de que $n$ sea grande, $n+d$ está muy cerca de $n$, por lo que el resultado de esa división será un valor cercano a $1$. Para tener una mayor acertividad, se puede calcular $\left( \frac{n}{n + d} \right)^e$, donde $e$ es un entero mayor que $1$.

## 2.2 Complemento
El complemento de un conjunto difuso demuestra el grado en el que un elemento **no pertenece** a dicho conjunto. Si la función característica $f_s(x)$ define el grado de pertenencia de un elemento $x$ al conjunto difuso $S$, por ende su complemento $f_{¬S}(x)$ se obtiene restando ese valor a 1, de la siguiente manera:

$f_{¬S}(x) = 1-f_S(x)$

Así, los elementos que tienen alto nivel de pertenencia en $S$ tendrán baja pertenencia en su complemento y viceversa.

## 2.3. Unión
La unión entre dos conjuntos difusos $A$ y $B$ representa el grado en el que un elemento pertenece **al menos a uno** de los dos conjuntos. En conjuntos difusos, la unión se define mediante el máximo de los grados de pertenencia individuales.

De manera formal, si $f_A(x)$ y $f_B(x)$ representan los grados de pertenencia de un elemento $x$ en $A$ y $B$ respectivamente, la unión se define como:

$f_{A∪B} = max(f_A(x), f_B(x))$

Así, la pertenencia de un elemento en la unión refleja el valor más alto de pertenencia entre ambos conjuntos.

## 2.4. Intersección
La intersección entre dos conjuntos difusos $A$ y $B$ representa el grado en el que un elemento pertenece **simultaneamente** a ambos conjuntos. En conjuntos difusos la intersección se define tomando el mínimo de los grados de pertenencia individuales.

De manera formal, si $f_A(x)$ y $f_B(x)$ son las funciones de pertenencia de los conjuntos $A$ y $B$, la intersección se define así:

$f_{A∩B} = min(f_A(x), f_B(x))$

De esta manera el grado de pertenencia en la intersección muestra el valor más bajo de pertenencia entre ambos conjuntos.

## 2.5. Inclusión
La inclusión entre dos conjuntos difusos $A$ y $B$ permite determinar si todos los elementos de $A$ están contenidos en $B$ en lo que a grado de pertenencia se refiere.

De manera formal se dice que $A⊆B$ si para todo elemento $x$ se cumple lo siguente:

$f_A(x) ≤ f_B(x)$

Esto puede interpretarse como que los elementos que tienen un alto grado de pertenencia en $A$ al menos tienen ese mismo grado en $B$.

## 2.6. Igualdad
La igualdad entre dos conjuntos difusos $A$ y $B$ se presenta cuando ambos conjuntos tienen **el mismo grado** de pertenencia para todos los elementos.

De manera formal dice que:

$A = B ⟺ f_A(x) = f_B(x), ∀x$

Esto significa que cada elemento pertenece a ambos conjuntos con exactamente la misma intensidad o grado de pertenencia.

---
# 3. Implementación
```scala
package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }

  def grande(d: Int, e: Int): ConjDifuso = {
    (n: Int) => {
      if (n <= 0) 0.0
      else Math.pow(n.toDouble / (n + d).toDouble, e.toDouble)
    }
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (x: Int) => 1.0 - c(x)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => Math.max(cd1(x), cd2(x))
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => Math.min(cd1(x), cd2(x))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def aux(x: Int): Boolean = {
      if (x > 1000) true
      else if (cd1(x) > cd2(x)) false
      else aux(x + 1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1)
  }
}
```
### Estructura:
- `pertenece`: Evalúa el grado de pertenencia de un elemento dentro de un conjunto difuso.
- `grande`: Determina si un número es grande según los parámetros $d$ y $e$, devolviendo una función difusa.
- `complemento`: Devuelve el conjunto complementario o inverso.
- `union`: Calcula el valor máximo de pertenencia entre dos conjuntos.
- `interseccion`: Calcula el valor mínimo de pertenencia entre dos conjuntos.
- `inclusion`: Utiliza recursión de cola para verificar si $A ⊆ B$ en un rango.
- `igualdad`: Verifica la equivalencia entre dos conjuntos comprobando con la inclusión.

### Flujo:
- Las operaciones difusas están expresadas con operaciones matemáticas simples.
- En el caso de `inclusion`, se utiliza recursión de cola (`@tailrec`) para comparar los valores de un dominio previamente indicado ($0 ≤ x ≤ 1000$).
- En el caso de `igualdad` se reutiliza la función `inclusion`, para reflejar el principio de equivalencia.

---