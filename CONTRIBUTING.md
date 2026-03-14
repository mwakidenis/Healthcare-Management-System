---
<p align="center" class="toc">
<strong><a href="#setup">Setup</a></strong>
|
<strong><a href="#running-lintingtests">Running linting/tests</a></strong>
|
<strong><a href="#writing-tests">Writing tests</a></strong>
|
<strong><a href="#debugging-code">Debugging code</a></strong>
|
<strong><a href="#internals">Internals</a></strong>
</p>
---

# Contributing

Contributions are always welcome, no matter how large or small!

Before contributing, please read the code of conduct.

If you want an already configured online IDE to contribute to this project, you can use Gitpod:

https://gitpod.io/#https://github.com/mwakidenis/Healthcare-Management-System

---

## Not sure where to start?

- If you aren't just making a documentation change, you'll probably want to learn a bit about a few topics.

  - **ASTs (Abstract Syntax Trees)**  
    https://en.wikipedia.org/wiki/Abstract_syntax_tree

  - You can explore AST structures using **AST Explorer**  
    https://astexplorer.net/

- When you feel ready to jump into the source code, start by checking project issues or TODO items.

- Follow development updates in the repository discussions or issues.

---

# Developing

### Requirements

Make sure the following tools are installed:

**Node.js**

```
node -v
```

Install Node from:

https://nodejs.org/en/download/

**Yarn**

Install Yarn:

https://yarnpkg.com/getting-started/install

---

# Setup

Fork the repository to your GitHub account.

Then run:

```sh
git clone https://github.com/<your-github-username>/Healthcare-Management-System
cd Healthcare-Management-System
```

Install dependencies:

```sh
npm install
```

or

```sh
yarn install
```

---

## Build Project

Build once:

```sh
npm run build
```

or

```sh
yarn build
```

Watch mode (auto rebuild):

```sh
npm run watch
```

---

# Running linting/tests

### Lint

Run linter:

```sh
npm run lint
```

Auto-fix lint errors:

```sh
npm run lint:fix
```

---

### Run All Tests

```sh
npm test
```

---

### Run Specific Test

Example:

```sh
npm test healthcare-module
```

---

### Run Tests With Debugger

Run Node debugger:

```sh
node --inspect-brk node_modules/.bin/jest
```

Then open Chrome DevTools to debug.

---

# Writing Tests

Tests are stored inside:

```
tests/
```

Example test:

```js
describe("Healthcare Management System", () => {

  test("should calculate patient age", () => {

    const age = 2025 - 2000

    expect(age).toBe(25)

  })

})
```

Run tests:

```sh
npm test
```

---

# Test Coverage

Run coverage:

```sh
npm run test:coverage
```

---

# Troubleshooting Tests

Common issues:

**Dependency updates**

Run:

```
npm install
```

again to refresh dependencies.

**Node version mismatch**

Check version:

```
node -v
```

---

# Debugging Code

A common approach is to debug using **Chrome DevTools**.

Example debugging code:

```js
function generate() {
  debugger
  return processData()
}
```

Run project:

```
node index.js
```

Then open Chrome DevTools debugger.

---

# Internals

Project structure example:

```
Healthcare-Management-System
│
├── src
│   ├── controllers
│   ├── models
│   ├── routes
│   └── services
│
├── tests
│
├── docs
│
├── package.json
├── README.md
└── CONTRIBUTING.md
```

Main logic lives in:

```
src/
```

---

# Contribution Workflow

1. Fork the repository
2. Create a feature branch

```
git checkout -b feature-name
```

3. Commit your changes

```
git commit -m "Add new healthcare feature"
```

4. Push branch

```
git push origin feature-name
```

5. Open a Pull Request

---

Thank you for contributing to the **Healthcare Management System** 🚀
