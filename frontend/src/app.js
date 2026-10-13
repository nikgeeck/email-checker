const rowsEl = document.getElementById("rows");
const statusEl = document.getElementById("status");
const lastResultEl = document.getElementById("last-result");
const formEl = document.getElementById("check-form");
const btnCheck = document.getElementById("btn-check");
const btnList = document.getElementById("btn-list");

function setStatus(message) {
  statusEl.textContent = message || "";
}

function badge(ok) {
  return `<span class="badge ${ok ? "ok" : "bad"}">${ok ? "да" : "нет"}</span>`;
}

function formatDate(iso) {
  if (!iso) return "—";
  try {
    return new Date(iso).toLocaleString("ru-RU");
  } catch {
    return iso;
  }
}

function renderRows(items) {
  if (!items.length) {
    rowsEl.innerHTML = `<tr><td colspan="6" class="empty">Записей пока нет</td></tr>`;
    return;
  }

  rowsEl.innerHTML = items
    .map(
      (item) => `
      <tr>
        <td>${item.id}</td>
        <td>${escapeHtml(item.name)}</td>
        <td>${escapeHtml(item.email)}</td>
        <td>${badge(item.formatValid)}</td>
        <td>${badge(item.mxValid)}</td>
        <td>${formatDate(item.checkedAt)}</td>
      </tr>`
    )
    .join("");
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;");
}

async function readErrorMessage(response) {
  const text = await response.text();
  if (!text) {
    return `Ошибка ${response.status}`;
  }
  try {
    const json = JSON.parse(text);
    if (json && typeof json.message === "string" && json.message.trim()) {
      return json.message;
    }
  } catch {
    // not JSON
  }
  return text;
}

async function loadCounterparties() {
  setStatus("Загрузка списка...");
  btnList.disabled = true;
  try {
    const response = await fetch("/api/counterparties");
    if (!response.ok) {
      throw new Error(await readErrorMessage(response));
    }
    const data = await response.json();
    renderRows(data);
    setStatus(`Загружено записей: ${data.length}`);
  } catch (error) {
    setStatus(`Не удалось загрузить список: ${error.message}`);
  } finally {
    btnList.disabled = false;
  }
}

async function checkEmail(event) {
  event.preventDefault();
  const name = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim();

  setStatus("Проверка email через web-service...");
  btnCheck.disabled = true;
  lastResultEl.classList.add("hidden");

  try {
    const response = await fetch("/api/counterparties/check", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email }),
    });
    if (!response.ok) {
      throw new Error(await readErrorMessage(response));
    }
    const data = await response.json();
    lastResultEl.textContent = JSON.stringify(data, null, 2);
    lastResultEl.classList.remove("hidden");
    setStatus(
      data.formatValid && data.mxValid
        ? "Email валиден (формат и MX)."
        : "Проверка завершена: есть замечания по формату или MX."
    );
    await loadCounterparties();
  } catch (error) {
    setStatus(`Ошибка проверки: ${error.message}`);
  } finally {
    btnCheck.disabled = false;
  }
}

formEl.addEventListener("submit", checkEmail);
btnList.addEventListener("click", loadCounterparties);

loadCounterparties();
