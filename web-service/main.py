import re
from typing import List

import dns.resolver
from fastapi import FastAPI
from pydantic import BaseModel, Field

app = FastAPI(title="Email Validator", version="1.0.0")

EMAIL_REGEX = re.compile(
    r"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@"
    r"[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
    r"(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$"
)


class ValidateRequest(BaseModel):
    email: str = Field(..., min_length=1, max_length=320)


class ValidateResponse(BaseModel):
    email: str
    formatValid: bool
    mxValid: bool
    mxHosts: List[str]


@app.get("/health")
def health():
    return {"status": "ok"}


def check_format(email: str) -> bool:
    return EMAIL_REGEX.fullmatch(email.strip()) is not None


def check_mx(email: str) -> tuple[bool, List[str]]:
    domain = email.rsplit("@", 1)[-1].strip().lower()
    if not domain:
        return False, []
    try:
        answers = dns.resolver.resolve(domain, "MX")
        hosts = sorted(
            {str(r.exchange).rstrip(".") for r in answers},
            key=str.lower,
        )
        return len(hosts) > 0, hosts
    except (dns.resolver.NXDOMAIN, dns.resolver.NoAnswer, dns.resolver.NoNameservers, dns.exception.Timeout):
        return False, []
    except Exception:
        return False, []


@app.post("/validate", response_model=ValidateResponse)
def validate_email(body: ValidateRequest):
    email = body.email.strip()
    format_valid = check_format(email)
    mx_hosts: List[str] = []
    mx_valid = False
    if format_valid:
        mx_valid, mx_hosts = check_mx(email)
    return ValidateResponse(
        email=email,
        formatValid=format_valid,
        mxValid=mx_valid,
        mxHosts=mx_hosts,
    )
