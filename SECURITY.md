# Security Policy

## Supported Versions

This is the list of versions of **Healthcare Management System (HMS)** which are
currently being supported with security updates.

| Version | Supported |
|--------|-----------|
| 8.x | :white_check_mark: |
| 7.x | :white_check_mark: [^1] |
| 6.x | :x: |

---

## Reporting a Vulnerability

To report a vulnerability please send an email with the details to:

mwakidenice@gmail.com

The vulnerability report should include:

- A proof-of-concept exploit (if available)
- Steps to reproduce the vulnerability
- Any additional information that can help assess the severity of the issue

---

## Disclosure Process

Once we assess the risk level, we will work together with the reporter on a fix or patch.

After the patch is ready and released, we will coordinate disclosure with the reporter and other stakeholders if necessary.

Security updates will be released as soon as possible to ensure the **Healthcare Management System** remains secure.

---

## Vulnerabilities in Dependencies

If you receive a security warning related to a dependency used in the **Healthcare Management System**, please verify the following before reporting:

1. The version of the dependency containing the security fix is **not compatible** with the semver range used in this project.
2. The vulnerability **actually affects this project's usage** of the dependency.

If condition **(1)** is true but **(2)** is false, the vulnerability may be considered **low risk** and may not require an immediate fix.

---

## Responsible Disclosure

Please **do not publicly disclose security vulnerabilities** until they have been reviewed and resolved.

---

Thanks for helping keep the **Healthcare Management System** secure 🔐
