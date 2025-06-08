# 🌍 Rota Segura

**Rota Segura** é um sistema simples e eficiente desenvolvido em **Java** para registrar, armazenar e monitorar pontos de risco geográfico com base em coordenadas geográficas, umidade do solo e inclinação do terreno.

> O objetivo é ajudar na tomada de decisões sobre rotas mais seguras, identificando áreas com risco potencial de deslizamento ou instabilidade.

---

## 📦 Funcionalidades

- 📍 Cadastro de coordenadas geográficas de pontos monitorados
- 🔐 Geração de `hash` exclusivo para cada coordenada
- 💧 Registro de medidas de **inclinação** e **umidade**
- 🔎 Busca por pontos a partir do `hash`
- ⚠️ Identificação de pontos em **risco**
- 📝 Leitura e gravação em arquivos `.txt` externos

---

## 🧠 Como funciona

Cada ponto monitorado recebe um código `hash` gerado a partir de partes da sua coordenada. Esses dados são armazenados em arquivos `.txt` para persistência mesmo após encerramento do programa.

O sistema também permite inserir novos pontos diretamente pelo terminal, com salvamento automático das informações.

---

## 🗂 Estrutura do Projeto
