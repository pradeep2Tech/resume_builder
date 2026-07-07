const API = '/api/v1';

const profileName = document.getElementById('profile-name');
const profileMeta = document.getElementById('profile-meta');
const cvSelect = document.getElementById('cv-select');
const dropZone = document.getElementById('drop-zone');
const jdFile = document.getElementById('jd-file');
const browseBtn = document.getElementById('browse-btn');
const fileName = document.getElementById('file-name');
const jdText = document.getElementById('jd-text');
const analyzeBtn = document.getElementById('analyze-btn');
const results = document.getElementById('results');
const errorEl = document.getElementById('error');

let activeTab = 'upload';
let selectedFile = null;
let defaultResumePath = '';

init();

async function init() {
  setupTabs();
  setupUpload();
  analyzeBtn.addEventListener('click', analyze);

  try {
    const [profile, cvs] = await Promise.all([
      fetchJson(`${API}/profile`),
      fetchJson(`${API}/cvs`)
    ]);
    profileName.textContent = profile.name || 'Resume Builder';
    profileMeta.textContent = [profile.title, profile.location].filter(Boolean).join(' · ');
    defaultResumePath = profile.defaultResumePath;

    cvSelect.innerHTML = '';
    cvs.forEach(cv => {
      const opt = document.createElement('option');
      opt.value = cv.path;
      opt.textContent = cv.slug;
      if (cv.path === defaultResumePath) opt.selected = true;
      cvSelect.appendChild(opt);
    });
    if (cvs.length === 0) {
      const opt = document.createElement('option');
      opt.value = defaultResumePath;
      opt.textContent = 'default';
      cvSelect.appendChild(opt);
    }
  } catch (e) {
    showError('Failed to load profile from career-master. Is the server running?');
  }
}

function setupTabs() {
  document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
      activeTab = tab.dataset.tab;
      document.querySelectorAll('.tab').forEach(t => t.classList.toggle('active', t === tab));
      document.getElementById('panel-upload').classList.toggle('active', activeTab === 'upload');
      document.getElementById('panel-paste').classList.toggle('active', activeTab === 'paste');
    });
  });
}

function setupUpload() {
  browseBtn.addEventListener('click', () => jdFile.click());
  jdFile.addEventListener('change', () => setFile(jdFile.files[0]));

  dropZone.addEventListener('dragover', e => {
    e.preventDefault();
    dropZone.classList.add('dragover');
  });
  dropZone.addEventListener('dragleave', () => dropZone.classList.remove('dragover'));
  dropZone.addEventListener('drop', e => {
    e.preventDefault();
    dropZone.classList.remove('dragover');
    if (e.dataTransfer.files.length) setFile(e.dataTransfer.files[0]);
  });
  dropZone.addEventListener('click', e => {
    if (e.target === browseBtn) return;
    jdFile.click();
  });
}

function setFile(file) {
  selectedFile = file;
  fileName.textContent = file ? file.name : '';
}

async function analyze() {
  hideError();
  analyzeBtn.disabled = true;
  analyzeBtn.textContent = 'Analyzing…';

  try {
    const resumePath = cvSelect.value || defaultResumePath;
    let response;

    if (activeTab === 'upload') {
      if (!selectedFile) {
        throw new Error('Select a job description file first.');
      }
      const form = new FormData();
      form.append('jobDescriptionFile', selectedFile);
      form.append('resumePath', resumePath);
      response = await fetch(`${API}/jd/analyze/upload`, { method: 'POST', body: form });
    } else {
      const text = jdText.value.trim();
      if (!text) {
        throw new Error('Paste job description text first.');
      }
      response = await fetch(`${API}/jd/analyze`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ jobDescription: text, resumePath })
      });
    }

    if (!response.ok) {
      const err = await response.json().catch(() => ({}));
      throw new Error(err.detail || `Request failed (${response.status})`);
    }

    renderResults(await response.json());
  } catch (e) {
    showError(e.message);
    results.classList.add('hidden');
  } finally {
    analyzeBtn.disabled = false;
    analyzeBtn.textContent = 'Analyze';
  }
}

function renderResults(data) {
  results.classList.remove('hidden');

  const score = data.matchScore ?? 0;
  document.getElementById('score-value').textContent = `${score}%`;
  document.getElementById('score-ring').style.setProperty('--score', score);
  document.getElementById('score-count').textContent =
    `${data.matchedCount ?? 0} / ${data.requiredCount ?? 0} required keywords matched`;

  const rec = document.getElementById('recommendation');
  if (score >= 80) {
    rec.textContent = 'Use existing CV — match meets 80% threshold';
    rec.className = 'recommendation ok';
  } else {
    rec.textContent = 'Tailor CV — add missing keywords to reach 80%';
    rec.className = 'recommendation warn';
  }

  document.getElementById('analysis-mode').textContent = data.analysisMode || 'rule-based';
  renderChips('matched-chips', data.matchedKeywords, 'No matches');
  renderChips('missing-chips', data.missingKeywords, 'None — great coverage');
  renderChips('nice-matched-chips', data.matchedNiceToHave, 'None');
  renderChips('nice-missing-chips', data.missingNiceToHave, 'None');
  renderList('projects-list', data.recommendedProjects);
  renderList('ats-flags', data.atsFlags);
}

function renderChips(id, items, emptyText) {
  const el = document.getElementById(id);
  el.innerHTML = '';
  if (!items || items.length === 0) {
    el.innerHTML = `<span class="muted">${emptyText}</span>`;
    return;
  }
  items.forEach(item => {
    const chip = document.createElement('span');
    chip.className = 'chip';
    chip.textContent = item;
    el.appendChild(chip);
  });
}

function renderList(id, items) {
  const el = document.getElementById(id);
  el.innerHTML = '';
  if (!items || items.length === 0) {
    const li = document.createElement('li');
    li.textContent = 'None';
    el.appendChild(li);
    return;
  }
  items.forEach(item => {
    const li = document.createElement('li');
    li.textContent = item;
    el.appendChild(li);
  });
}

async function fetchJson(url) {
  const response = await fetch(url);
  if (!response.ok) throw new Error(`Failed: ${url}`);
  return response.json();
}

function showError(msg) {
  errorEl.textContent = msg;
  errorEl.classList.remove('hidden');
}

function hideError() {
  errorEl.classList.add('hidden');
}
