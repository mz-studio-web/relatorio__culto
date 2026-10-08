# Relatório do Culto — Igreja Evangélica Assembleia de Deus (Alfa e Omega do Trevo)

Aplicação web para registar as **presenças** e gerar os **relatórios** dos cultos.
Funciona **offline**, instala-se no telemóvel como uma app e guarda o histórico no próprio aparelho.

## ✨ Funcionalidades
- Contagem de presenças por categoria: **Pais, Mães, Jovens, Adolescentes, Crianças** (com total ao vivo)
- Subtotais informativos (não somam ao total): **Anciãos, Visitantes, Novos convertidos**
- **Tipo de culto**: Santa Ceia · 1º Domingo / Culto Normal / Estudo Bíblico
- Campos da **Santa Ceia** (só no 1º Domingo): *Membros participantes* e *Tomaram a Ceia*
- **Dados do culto**: Dirigente, Coristas, Músicos, Protocolos, Leitura oficial, Pregador, Tradutor, Tema, Texto Base
- **Visitantes** com nome e proveniência
- **Relatório do Culto** em PDF e **Relatório Geral** (estatística por período)
- **Gravação automática** no histórico do dispositivo + exportação **CSV** (backup)

## 🌐 Publicar com GitHub Pages
1. Crie um repositório e envie estes ficheiros (pelo menos o `index.html` e o `.nojekyll`).
2. Vá a **Settings → Pages**.
3. Em *Build and deployment* → *Source*: **Deploy from a branch**.
4. *Branch*: **main** e pasta **/ (root)** → **Save**.
5. Aguarde 1–2 minutos. O link aparece no topo da página do Pages
   (ex.: `https://SEU-UTILIZADOR.github.io/NOME-DO-REPOSITORIO/`).
6. No telemóvel, abra esse link e escolha **"Adicionar ao ecrã inicial"**.

> O ficheiro `.nojekyll` garante que o GitHub Pages serve o site tal como está.

## 📱 App Android (opcional)
A pasta `android/RelatorioDoCulto` contém um projeto pronto para abrir no **Android Studio**
e compilar num APK. Veja `android/RelatorioDoCulto/COMO-COMPILAR.txt`.

## 🔒 Dados
O histórico é guardado **localmente** em cada dispositivo (não é partilhado entre telemóveis).
Use a exportação CSV como cópia de segurança.

## 📂 Estrutura
```
index.html                      → a aplicação (abre no navegador)
.nojekyll                       → configuração do GitHub Pages
android/RelatorioDoCulto/       → projeto Android (APK)
exemplos/                       → exemplo de relatório (PDF) e estrutura (Word)
```
