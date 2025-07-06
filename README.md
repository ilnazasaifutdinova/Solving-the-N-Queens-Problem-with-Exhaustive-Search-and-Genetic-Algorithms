# 🧩 Solving the N-Queens Problem with Exhaustive Search and Genetic Algorithms

This repository presents a comparative study of four algorithms used to solve the **N-Queens problem**:

- **Depth-First Search (DFS)**
- **Hill Climbing (HC)**
- **Simulated Annealing (SA)**
- **Genetic Algorithm (GA)**

Each algorithm is evaluated in terms of **solution success rate**, **runtime**, and **memory usage** for various board sizes (N = 10, 30, 50, 100, 200).

## 🧠 Project Motivation

The N-Queens problem is a classic example of a **constraint satisfaction** and **combinatorial optimization** problem. This project explores different strategies to solve it using both **exhaustive** and **heuristic** methods.

## 🛠️ Running the Project

### Clone the repository:
```bash
git clone https://github.com/ilnazasaifutdinova/Solving-the-N-Queens-Problem-with-Exhaustive-Search-and-Genetic-Algorithms.git
```
### Install requirements:
```bash
pip install -r requirements.txt
```
### Run an algorithm (example for DFS):
```bash
python src/dfs.py --n 8
```

## 📌 Key Findings
DFS is reliable for small N but quickly becomes infeasible as N grows.

Hill Climbing is fast but often gets stuck in local minima.

Simulated Annealing performs better due to probabilistic jumps.

Genetic Algorithm provides the best trade-off between speed and success rate for large N.

