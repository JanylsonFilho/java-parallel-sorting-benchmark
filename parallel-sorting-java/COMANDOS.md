# Ordenação Paralela e Concorrente em Java

Projeto para comparar versões seriais e paralelas de quatro algoritmos de ordenação:

- Bubble Sort
- Selection Sort
- Merge Sort
- Quick Sort

O objetivo é analisar o desempenho dos algoritmos em execuções seriais e paralelas, variando:

- tamanho do vetor;
- tipo de entrada;
- quantidade de threads;
- número de amostras por execução.

Os resultados são salvos em arquivos CSV e podem ser analisados por meio de gráficos dinâmicos em Jupyter Notebook.

---

## Estrutura do projeto

```text
ordenacao-paralela-java-4-algoritmos/
├── src/
│   ├── BenchmarkResult.java
│   ├── BenchmarkRunner.java
│   ├── BubbleSortParallel.java
│   ├── BubbleSortSerial.java
│   ├── CsvWriter.java
│   ├── DataGenerator.java
│   ├── DataType.java
│   ├── Main.java
│   ├── MergeSortParallel.java
│   ├── MergeSortSerial.java
│   ├── QuickSortParallel.java
│   ├── QuickSortSerial.java
│   ├── SelectionSortParallel.java
│   ├── SelectionSortSerial.java
│   └── SortAlgorithm.java
├── out/
├── analise_graficos_dinamicos.ipynb
└── README.md
```

---

## Como compilar pelo terminal

Dentro da pasta raiz do projeto, execute:

```bash
javac -d out src/*.java
```

Esse comando compila todos os arquivos `.java` da pasta `src` e envia os arquivos compilados para a pasta `out`.

---

## Como executar pelo terminal

Depois de compilar, execute:

```bash
java -cp out Main
```

O programa executará os testes de desempenho e gerará o arquivo CSV em:

```text
out/resultados_ordenacao.csv
```

Esse arquivo contém os resultados das execuções dos algoritmos.

---

## Como usar no IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ.
2. Caso necessário, clique com o botão direito na pasta `src`.
3. Selecione `Mark Directory as`.
4. Escolha `Sources Root`.
5. Execute a classe `Main.java`.
6. Após a execução, o CSV será gerado em:

```text
out/resultados_ordenacao.csv
```

---

## Algoritmos implementados

### Bubble Sort

O Bubble Sort é um algoritmo simples baseado na comparação de pares adjacentes.

Foram implementadas duas versões:

- `BubbleSortSerial`
- `BubbleSortParallel`

A versão paralela foi implementada usando uma abordagem baseada em **Odd-Even Transposition Sort**, que compara pares independentes em fases pares e ímpares.

Essa versão é útil para fins didáticos, mas o Bubble Sort não é naturalmente eficiente para paralelização, pois possui muitas dependências sequenciais e custo de sincronização.

---

### Selection Sort

O Selection Sort seleciona repetidamente o menor elemento do trecho ainda não ordenado do vetor.

Foram implementadas duas versões:

- `SelectionSortSerial`
- `SelectionSortParallel`

Na versão paralela, a busca pelo menor elemento é dividida entre várias threads.

Assim como o Bubble Sort, o Selection Sort também não é naturalmente muito eficiente para paralelismo, pois cada iteração depende da anterior.

---

### Merge Sort

O Merge Sort utiliza a estratégia de **dividir para conquistar**.

Foram implementadas duas versões:

- `MergeSortSerial`
- `MergeSortParallel`

A versão paralela utiliza `ForkJoinPool`, dividindo o vetor em subproblemas menores que podem ser processados simultaneamente.

Esse algoritmo tende a apresentar melhor desempenho paralelo em entradas maiores.

---

### Quick Sort

O Quick Sort também utiliza a estratégia de divisão do problema.

Foram implementadas duas versões:

- `QuickSortSerial`
- `QuickSortParallel`

A versão paralela utiliza `ForkJoinPool`. Após o particionamento do vetor, as duas partições são ordenadas em paralelo.

Assim como o Merge Sort, o Quick Sort tende a se beneficiar mais do paralelismo em entradas maiores.

---

## Tipos de entrada testados

O projeto gera diferentes tipos de vetores para análise:

```text
RANDOM
SORTED
REVERSED
NEARLY_SORTED
```

Significado:

- `RANDOM`: vetor com valores aleatórios;
- `SORTED`: vetor já ordenado;
- `REVERSED`: vetor em ordem reversa;
- `NEARLY_SORTED`: vetor quase ordenado.

---

## Quantidade de amostras

Para cada configuração de teste, são executadas 5 amostras.

Isso permite calcular:

- tempo médio;
- desvio padrão;
- comparação entre versões seriais e paralelas;
- speedup;
- eficiência paralela.

---

## Quantidade de threads

O programa detecta automaticamente a quantidade de processadores disponíveis na máquina por meio de:

```java
Runtime.getRuntime().availableProcessors()
```

Depois, executa as versões paralelas variando a quantidade de threads.

Exemplo de saída:

```text
Processadores disponíveis: 16
Threads testadas: [2, 4, 8, 16]
```

---

## Arquivo CSV gerado

Após a execução do programa Java, será criado o arquivo:

```text
out/resultados_ordenacao.csv
```

O CSV contém colunas como:

```text
algorithm,base_algorithm,execution_type,threads,size,data_type,sample,time_ns,time_ms,sorted
```

Descrição das principais colunas:

- `algorithm`: nome da implementação executada;
- `base_algorithm`: nome base do algoritmo;
- `execution_type`: tipo de execução, serial ou paralela;
- `threads`: quantidade de threads utilizadas;
- `size`: tamanho do vetor;
- `data_type`: tipo de entrada;
- `sample`: número da amostra;
- `time_ns`: tempo em nanossegundos;
- `time_ms`: tempo em milissegundos;
- `sorted`: indica se o vetor foi ordenado corretamente.

---

## Gráficos dinâmicos com Jupyter Notebook

O projeto também possui um notebook Jupyter para gerar gráficos dinâmicos a partir do CSV produzido pelo Java.

Arquivo do notebook:

```text
analise_graficos_dinamicos.ipynb
```

No notebook, a variável padrão é:

```python
csv_path = Path("out/resultados_ordenacao.csv")
```

Portanto, antes de abrir o notebook, execute primeiro o programa Java para gerar o CSV:

```bash
javac -d out src/*.java
java -cp out Main
```

Após isso, o arquivo abaixo será criado:

```text
out/resultados_ordenacao.csv
```

---

## Como rodar o notebook fora do IntelliJ

Caso o IntelliJ apresente erro ao executar arquivos `.ipynb`, é possível rodar o Jupyter diretamente pelo terminal.

Dentro da pasta do projeto, execute:

```bash
python3 -m venv .venv
```

Ative o ambiente virtual:

```bash
source .venv/bin/activate
```

Instale as dependências necessárias:

```bash
pip install notebook pandas plotly ipywidgets nbformat
```

Depois, abra o Jupyter Notebook:

```bash
jupyter notebook
```

O navegador será aberto automaticamente.

Caso não abra, copie o link exibido no terminal e cole no navegador.

No navegador, abra o arquivo:

```text
analise_graficos_dinamicos.ipynb
```

Depois execute as células do notebook.

> Observação: o notebook deve ser executado a partir da raiz do projeto, pois ele espera encontrar o CSV no caminho `out/resultados_ordenacao.csv`.

---

## Fluxo completo recomendado

```text
1. Abrir a pasta do projeto
2. Compilar os arquivos Java
3. Executar a classe Main
4. Gerar o arquivo out/resultados_ordenacao.csv
5. Abrir o Jupyter Notebook pelo terminal
6. Executar o arquivo analise_graficos_dinamicos.ipynb
7. Analisar os gráficos dinâmicos gerados
```

Comandos:

```bash
javac -d out src/*.java
java -cp out Main
python3 -m venv .venv
source .venv/bin/activate
pip install notebook pandas plotly ipywidgets nbformat
jupyter notebook
```

---

## Exportar o notebook para HTML

Também é possível exportar os gráficos para um arquivo HTML.

Dentro da pasta do projeto, execute:

```bash
jupyter nbconvert --to html analise_graficos_dinamicos.ipynb
```

Isso irá gerar o arquivo:

```text
analise_graficos_dinamicos.html
```

Esse arquivo pode ser aberto no navegador e enviado junto com o trabalho.

---

## Arquivos gerados pelo notebook

Ao final da execução do notebook, são gerados dois arquivos adicionais:

```text
out/resumo_estatistico.csv
out/speedup_eficiencia.csv
```

### `resumo_estatistico.csv`

Contém os dados agrupados com:

- tempo médio;
- desvio padrão;
- número de amostras.

### `speedup_eficiencia.csv`

Contém os cálculos de:

- speedup;
- eficiência paralela.

---

## Conceitos usados na análise

### Speedup

O speedup compara o tempo da versão serial com o tempo da versão paralela:

```text
Speedup = Tempo Serial / Tempo Paralelo
```

Interpretação:

- `Speedup > 1`: a versão paralela foi mais rápida;
- `Speedup = 1`: desempenho equivalente;
- `Speedup < 1`: a versão paralela foi mais lenta.

---

### Eficiência paralela

A eficiência paralela mede o aproveitamento das threads:

```text
Eficiência = Speedup / Número de Threads
```

Quanto mais próxima de 1, melhor foi o aproveitamento das threads.

---

## Observações sobre desempenho

Nem sempre aumentar a quantidade de threads melhora o desempenho.

Algoritmos como Bubble Sort e Selection Sort possuem maior dependência sequencial e maior custo de sincronização, por isso podem apresentar desempenho paralelo inferior ao serial.

Já algoritmos como Merge Sort e Quick Sort tendem a apresentar melhor aproveitamento do paralelismo, principalmente em vetores maiores, pois permitem dividir o problema em subproblemas independentes.

---

## Resultado esperado

Ao final da execução, o projeto permite:

- comparar algoritmos seriais e paralelos;
- analisar o impacto do tamanho da entrada;
- analisar o impacto do tipo de entrada;
- testar diferentes quantidades de threads;
- gerar arquivos CSV com os resultados;
- gerar gráficos dinâmicos no Jupyter Notebook;
- calcular speedup e eficiência paralela.
