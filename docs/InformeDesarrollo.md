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