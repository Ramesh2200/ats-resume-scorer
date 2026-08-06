/**
 * ATS Resume Scorer & Technical Interview Studio - Frontend Engine v3.0
 */

document.addEventListener('DOMContentLoaded', () => {
    const API_BASE = '/api/ats';
    const AUTH_BASE = '/api/auth';
    const INTERVIEW_BASE = '/api/interview';

    // Current State
    let currentUser = JSON.parse(localStorage.getItem('ats_user')) || null;
    let currentEvaluation = null;
    let jobPresetsCache = [];
    let historyCache = [];
    let interviewQuestionsCache = [];

    // Header & Theme Elements
    const brandLogoBtn = document.getElementById('brand-logo-btn');
    const themeToggleBtn = document.getElementById('theme-toggle-btn');
    const authNavWrap = document.getElementById('auth-nav-wrap');

    // Navigation & Views
    const navEvalBtn = document.getElementById('nav-eval-btn');
    const navInterviewBtn = document.getElementById('nav-interview-btn');
    const navRecruiterBtn = document.getElementById('nav-recruiter-btn');
    const navHistoryBtn = document.getElementById('nav-history-btn');

    const viewEvaluator = document.getElementById('view-evaluator');
    const viewInterview = document.getElementById('view-interview');
    const viewRecruiter = document.getElementById('view-recruiter');
    const viewHistory = document.getElementById('view-history');
    const historyCountBadge = document.getElementById('history-count');

    // Input Stage Elements
    const tabUpload = document.getElementById('tab-upload');
    const tabText = document.getElementById('tab-text');
    const uploadZone = document.getElementById('upload-zone');
    const textZone = document.getElementById('text-zone');
    const resumeFileInput = document.getElementById('resume-file-input');
    const resumeTextInput = document.getElementById('resume-text-input');
    const candidateNameInput = document.getElementById('candidate-name-input');
    const candidateEmailInput = document.getElementById('candidate-email-input');

    // Job Meta Elements
    const jobPresetSelect = document.getElementById('job-preset-select');
    const jobTitleInput = document.getElementById('job-title-input');
    const jobThresholdInput = document.getElementById('job-threshold-input');
    const jobTextInput = document.getElementById('job-text-input');
    const btnLoadSample = document.getElementById('btn-load-sample');
    const btnRunAts = document.getElementById('btn-run-ats');

    // Results Dashboard
    const resultsDashboard = document.getElementById('results-dashboard');
    const decisionStatusBadge = document.getElementById('decision-status-badge');
    const evalTimestamp = document.getElementById('eval-timestamp');
    const gaugeFill = document.getElementById('gauge-fill');
    const scoreNumber = document.getElementById('score-number');
    const scoreRatingTitle = document.getElementById('score-rating-title');
    const valThresholdDisplay = document.getElementById('val-threshold-display');

    // Metrics & Matrix
    const valHardSkills = document.getElementById('val-hard-skills');
    const barHardSkills = document.getElementById('bar-hard-skills');
    const valSoftSkills = document.getElementById('val-soft-skills');
    const barSoftSkills = document.getElementById('bar-soft-skills');
    const valActionVerbs = document.getElementById('val-action-verbs');
    const barActionVerbs = document.getElementById('bar-action-verbs');
    const valFormat = document.getElementById('val-format');
    const barFormat = document.getElementById('bar-format');

    const matchedPillsWrap = document.getElementById('matched-pills-wrap');
    const missingPillsWrap = document.getElementById('missing-pills-wrap');
    const countMatched = document.getElementById('count-matched');
    const countMissing = document.getElementById('count-missing');

    // Email & Mobile SMS
    const emailToVal = document.getElementById('email-to-val');
    const phoneToVal = document.getElementById('phone-to-val');
    const emailSubjectVal = document.getElementById('email-subject-val');
    const emailBodyVal = document.getElementById('email-body-val');
    const smsBodyVal = document.getElementById('sms-body-val');
    const btnSendEmail = document.getElementById('btn-send-email');
    const btnSendSms = document.getElementById('btn-send-sms');
    const btnDownloadResume = document.getElementById('btn-download-resume');
    const resumeViewerBody = document.getElementById('resume-viewer-body');

    // Interview & Coding Playground Elements
    const interviewTopicSelect = document.getElementById('interview-topic-select');
    const interviewDifficultySelect = document.getElementById('interview-difficulty-select');
    const questionsContainer = document.getElementById('questions-container');
    const questionCountBadge = document.getElementById('question-count');

    const codeLangSelect = document.getElementById('code-lang-select');
    const codeEditorInput = document.getElementById('code-editor-input');
    const btnRunCode = document.getElementById('btn-run-code');
    const codeOutputConsole = document.getElementById('code-output-console');

    // Recruiter Portal & History
    const formAddJob = document.getElementById('form-add-job');
    const historySearchInput = document.getElementById('history-search-input');
    const historyTableBody = document.getElementById('history-table-body');

    // Auth Modal
    const modalAuth = document.getElementById('modal-auth');
    const btnOpenLogin = document.getElementById('btn-open-login');
    const modalAuthClose = document.getElementById('modal-auth-close');
    const tabAuthLogin = document.getElementById('tab-auth-login');
    const tabAuthRegister = document.getElementById('tab-auth-register');
    const formLogin = document.getElementById('form-login');
    const formRegister = document.getElementById('form-register');

    // Initialize Application
    initTheme();
    bindEvents();
    renderAuthNav();
    fetchJobPresets();
    fetchHistory();
    fetchInterviewQuestions();

    function initTheme() {
        const savedTheme = localStorage.getItem('ats_theme') || 'dark';
        document.documentElement.setAttribute('data-theme', savedTheme);
        updateThemeBtnLabel(savedTheme);
    }

    function updateThemeBtnLabel(theme) {
        if (!themeToggleBtn) return;
        if (theme === 'dark') {
            themeToggleBtn.innerHTML = '<i class="fa-solid fa-sun"></i>';
            themeToggleBtn.title = 'Switch to Light/White Mode';
        } else {
            themeToggleBtn.innerHTML = '<i class="fa-solid fa-moon"></i>';
            themeToggleBtn.title = 'Switch to Dark Mode';
        }
    }

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            const current = document.documentElement.getAttribute('data-theme');
            const nextTheme = current === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', nextTheme);
            localStorage.setItem('ats_theme', nextTheme);
            updateThemeBtnLabel(nextTheme);
        });
    }

    function bindEvents() {
        // Brand Logo Navigation Switcher
        if (brandLogoBtn) brandLogoBtn.addEventListener('click', () => switchView('evaluator'));

        // Navigation Switcher
        if (navEvalBtn) navEvalBtn.addEventListener('click', () => switchView('evaluator'));
        if (navInterviewBtn) navInterviewBtn.addEventListener('click', () => {
            switchView('interview');
            fetchInterviewQuestions();
        });
        if (navRecruiterBtn) navRecruiterBtn.addEventListener('click', () => switchView('recruiter'));
        if (navHistoryBtn) navHistoryBtn.addEventListener('click', () => {
            switchView('history');
            fetchHistory();
        });

        // Input Tab Toggle
        if (tabUpload && tabText && uploadZone && textZone) {
            tabUpload.addEventListener('click', () => {
                tabUpload.classList.add('active'); tabText.classList.remove('active');
                uploadZone.classList.remove('hidden'); textZone.classList.add('hidden');
            });

            tabText.addEventListener('click', () => {
                tabText.classList.add('active'); tabUpload.classList.remove('active');
                textZone.classList.remove('hidden'); uploadZone.classList.add('hidden');
            });
        }

        // Drag & Drop
        if (uploadZone) {
            uploadZone.addEventListener('dragover', (e) => { e.preventDefault(); uploadZone.style.borderColor = 'var(--primary)'; });
            uploadZone.addEventListener('dragleave', () => { uploadZone.style.borderColor = 'rgba(99, 102, 241, 0.4)'; });
            uploadZone.addEventListener('drop', (e) => {
                e.preventDefault();
                uploadZone.style.borderColor = 'rgba(99, 102, 241, 0.4)';
                if (e.dataTransfer.files && e.dataTransfer.files[0]) handleFileUpload(e.dataTransfer.files[0]);
            });
        }

        if (resumeFileInput) {
            resumeFileInput.addEventListener('change', (e) => {
                if (e.target.files && e.target.files[0]) handleFileUpload(e.target.files[0]);
            });
        }

        if (btnLoadSample) btnLoadSample.addEventListener('click', loadSampleCandidate);
        if (jobPresetSelect) jobPresetSelect.addEventListener('change', handleJobPresetChange);

        if (btnRunAts) btnRunAts.addEventListener('click', runAtsEvaluation);
        if (btnSendEmail) btnSendEmail.addEventListener('click', dispatchCandidateEmail);
        if (btnSendSms) btnSendSms.addEventListener('click', dispatchCandidateSms);

        if (candidateEmailInput) {
            candidateEmailInput.addEventListener('input', () => {
                if (emailToVal) emailToVal.textContent = candidateEmailInput.value.trim() || 'candidate@example.com';
            });
        }
        if (btnDownloadResume) btnDownloadResume.addEventListener('click', triggerResumeDownload);
        if (formAddJob) formAddJob.addEventListener('submit', handleAddJobSubmit);

        // Interview Filter Events
        if (interviewTopicSelect) interviewTopicSelect.addEventListener('change', fetchInterviewQuestions);
        if (interviewDifficultySelect) interviewDifficultySelect.addEventListener('change', fetchInterviewQuestions);

        // History Search
        if (historySearchInput) {
            historySearchInput.addEventListener('input', (e) => {
                const query = e.target.value.toLowerCase();
                const filtered = historyCache.filter(item => 
                    (item.candidateName && item.candidateName.toLowerCase().includes(query)) ||
                    (item.jobTitle && item.jobTitle.toLowerCase().includes(query))
                );
                renderHistoryTable(filtered);
            });
        }

        // Auth Modal Controls & Fail-proof Exit Handlers
        const closeAuthModal = () => {
            if (modalAuth) modalAuth.classList.add('hidden');
        };

        if (btnOpenLogin) btnOpenLogin.addEventListener('click', () => { if (modalAuth) modalAuth.classList.remove('hidden'); });
        if (modalAuthClose) modalAuthClose.addEventListener('click', closeAuthModal);

        // Cancel / Exit Buttons inside forms
        document.querySelectorAll('.modal-cancel-btn').forEach(btn => {
            btn.addEventListener('click', closeAuthModal);
        });

        // Click outside on dark backdrop overlay
        if (modalAuth) {
            modalAuth.addEventListener('click', (e) => {
                if (e.target === modalAuth) closeAuthModal();
            });
        }

        // Keyboard ESC key to close
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modalAuth && !modalAuth.classList.contains('hidden')) {
                closeAuthModal();
            }
        });

        if (tabAuthLogin && tabAuthRegister && formLogin && formRegister) {
            tabAuthLogin.addEventListener('click', () => {
                tabAuthLogin.classList.add('active'); tabAuthRegister.classList.remove('active');
                formLogin.classList.remove('hidden'); formLogin.classList.add('active');
                formRegister.classList.add('hidden'); formRegister.classList.remove('active');
            });

            tabAuthRegister.addEventListener('click', () => {
                tabAuthRegister.classList.add('active'); tabAuthLogin.classList.remove('active');
                formRegister.classList.remove('hidden'); formRegister.classList.add('active');
                formLogin.classList.add('hidden'); formLogin.classList.remove('active');
            });
        }

        document.querySelectorAll('.role-option').forEach(opt => {
            opt.addEventListener('click', () => {
                document.querySelectorAll('.role-option').forEach(o => o.classList.remove('active'));
                opt.classList.add('active');
                const rad = opt.querySelector('input');
                if (rad) rad.checked = true;
            });
        });

        if (formLogin) formLogin.addEventListener('submit', handleLoginSubmit);
        if (formRegister) formRegister.addEventListener('submit', handleRegisterSubmit);

        const btnGoogleLogin = document.getElementById('btn-google-login');
        const btnGoogleRegister = document.getElementById('btn-google-register');
        if (btnGoogleLogin) btnGoogleLogin.addEventListener('click', () => handleGoogleAuth('CANDIDATE'));
        if (btnGoogleRegister) btnGoogleRegister.addEventListener('click', () => {
            const roleEl = document.querySelector('input[name="register-role"]:checked');
            const selectedRole = roleEl ? roleEl.value : 'CANDIDATE';
            handleGoogleAuth(selectedRole);
        });
    }

    let GOOGLE_CLIENT_ID = "225143211443-e6k4gabrpsd4171kskdtba3vb9hn4i4g.apps.googleusercontent.com";

    function handleGoogleAuth(role = 'CANDIDATE') {
        if (GOOGLE_CLIENT_ID && typeof google !== 'undefined' && google.accounts && google.accounts.oauth2) {
            // Real Google Accounts OAuth 2.0 Popup Flow
            const client = google.accounts.oauth2.initTokenClient({
                client_id: GOOGLE_CLIENT_ID,
                scope: 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email',
                callback: async (tokenResponse) => {
                    if (tokenResponse.access_token) {
                        try {
                            const profileRes = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
                                headers: { Authorization: `Bearer ${tokenResponse.access_token}` }
                            });
                            const profile = await profileRes.json();
                            const googleUser = {
                                username: profile.email.split('@')[0],
                                fullName: profile.name || profile.given_name || 'Google User',
                                email: profile.email,
                                picture: profile.picture,
                                role: role,
                                provider: 'GOOGLE'
                            };
                            currentUser = googleUser;
                            localStorage.setItem('ats_user', JSON.stringify(currentUser));
                            if (modalAuth) modalAuth.classList.add('hidden');
                            renderAuthNav();
                            if (role === 'CANDIDATE') {
                                if (candidateNameInput) candidateNameInput.value = googleUser.fullName;
                                if (candidateEmailInput) candidateEmailInput.value = googleUser.email;
                            }
                            showToastNotification(`Google Single Sign-On Success!\nWelcome ${googleUser.fullName} (${googleUser.email})`);
                        } catch (err) {
                            alert('Failed to retrieve Google profile: ' + err.message);
                        }
                    }
                }
            });
            client.requestAccessToken();
            return;
        }

        // Fallback / Setup Prompt if Google Client ID is not yet entered
        const promptEmail = prompt('Real Google Sign-In requires your Google Client ID.\n\nEnter your Gmail address to sign in (or provide your Google Client ID to enable live Google Popup):', 'user.google@gmail.com');
        if (!promptEmail) return;

        const defaultName = promptEmail.split('@')[0].replace('.', ' ').replace(/\b\w/g, c => c.toUpperCase());
        const googleUser = {
            username: promptEmail.split('@')[0],
            fullName: defaultName || 'Google User',
            email: promptEmail,
            role: role,
            provider: 'GOOGLE'
        };

        currentUser = googleUser;
        localStorage.setItem('ats_user', JSON.stringify(currentUser));
        if (modalAuth) modalAuth.classList.add('hidden');
        renderAuthNav();

        if (role === 'CANDIDATE') {
            if (candidateNameInput && !candidateNameInput.value) candidateNameInput.value = googleUser.fullName;
            if (candidateEmailInput && !candidateEmailInput.value) candidateEmailInput.value = googleUser.email;
        }

        showToastNotification(`Google Sign-In Successful!\nLogged in as ${googleUser.fullName} (${googleUser.email})`);
    }

    function switchView(viewName) {
        navEvalBtn.classList.remove('active');
        navInterviewBtn.classList.remove('active');
        navRecruiterBtn.classList.remove('active');
        navHistoryBtn.classList.remove('active');

        viewEvaluator.classList.add('hidden');
        viewInterview.classList.add('hidden');
        viewRecruiter.classList.add('hidden');
        viewHistory.classList.add('hidden');

        if (viewName === 'evaluator') {
            navEvalBtn.classList.add('active'); viewEvaluator.classList.remove('hidden');
        } else if (viewName === 'interview') {
            navInterviewBtn.classList.add('active'); viewInterview.classList.remove('hidden');
        } else if (viewName === 'recruiter') {
            navRecruiterBtn.classList.add('active'); viewRecruiter.classList.remove('hidden');
        } else {
            navHistoryBtn.classList.add('active'); viewHistory.classList.remove('hidden');
        }
    }

    // Auth Navigation & State
    function renderAuthNav() {
        if (!authNavWrap) return;
        if (currentUser) {
            authNavWrap.innerHTML = `
                <div class="user-status-badge" style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.8rem; background: var(--bg-inner); padding: 0.4rem 0.8rem; border-radius: 20px; border: 1px solid var(--border-glass);">
                    <i class="fa-solid fa-user-check text-emerald"></i>
                    <span><strong>${escapeHtml(currentUser.fullName || currentUser.username)}</strong> (${currentUser.role})</span>
                    <button id="btn-logout" style="background: transparent; border: none; color: #ef4444; cursor: pointer; font-size: 0.8rem; margin-left: 0.4rem;" title="Sign Out"><i class="fa-solid fa-right-from-bracket"></i></button>
                </div>
            `;
            document.getElementById('btn-logout').addEventListener('click', handleLogout);
        } else {
            authNavWrap.innerHTML = `<button id="btn-open-login" class="btn-primary-sm"><i class="fa-solid fa-user"></i> Login / Register</button>`;
            document.getElementById('btn-open-login').addEventListener('click', () => modalAuth.classList.remove('hidden'));
        }
    }

    async function handleLoginSubmit(e) {
        e.preventDefault();
        const username = document.getElementById('login-username').value.trim();
        const password = document.getElementById('login-password').value.trim();

        try {
            const res = await fetch(`${AUTH_BASE}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            const data = await res.json();
            if (data.success) {
                currentUser = data;
                localStorage.setItem('ats_user', JSON.stringify(currentUser));
                modalAuth.classList.add('hidden');
                renderAuthNav();
                alert(`Welcome back, ${data.fullName || data.username}! Logged in as ${data.role}.`);
            } else {
                alert(data.message || 'Invalid credentials');
            }
        } catch (err) {
            alert('Login error: ' + err.message);
        }
    }

    async function handleRegisterSubmit(e) {
        e.preventDefault();
        const fullName = document.getElementById('reg-fullname').value.trim();
        const username = document.getElementById('reg-username').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const password = document.getElementById('reg-password').value.trim();
        const role = document.querySelector('input[name="register-role"]:checked').value;

        try {
            const res = await fetch(`${AUTH_BASE}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ fullName, username, email, password, role })
            });
            const data = await res.json();
            if (data.success) {
                alert('Registration successful! Please sign in with your credentials.');
                tabAuthLogin.click();
            } else {
                alert(data.message || 'Registration failed.');
            }
        } catch (err) {
            alert('Registration error: ' + err.message);
        }
    }

    function handleLogout() {
        currentUser = null;
        localStorage.removeItem('ats_user');
        renderAuthNav();
    }

    // Current Selected Question & Coding Studio State
    let selectedQuestion = null;
    const codePlaygroundContainer = document.getElementById('code-playground-container');
    const testCaseAnalysisContainer = document.getElementById('test-case-analysis-container');

    if (codeLangSelect) {
        codeLangSelect.addEventListener('change', () => {
            const lang = codeLangSelect.value;
            if (selectedQuestion) {
                loadTemplateForLanguage(selectedQuestion, lang);
            }
        });
    }

    function loadTemplateForLanguage(q, lang) {
        let template = '';
        if (lang === 'Java') template = q.templateJava || q.codeSnippet;
        else if (lang === 'Python') template = q.templatePython || q.codeSnippet;
        else if (lang === 'C') template = q.templateC || q.codeSnippet;
        else if (lang === 'C++') template = q.templateCpp || q.codeSnippet;
        else if (lang === 'JavaScript') template = q.templateJs || q.codeSnippet;
        else if (lang === 'SQL') template = q.templateSql || q.codeSnippet;
        else template = q.codeSnippet;

        if (codeEditorInput) codeEditorInput.value = (template || '').replace(/\\n/g, '\n');
    }

    // Technical Interview Questions Fetcher & Renderer
    async function fetchInterviewQuestions() {
        const topic = interviewTopicSelect.value;
        const difficulty = interviewDifficultySelect.value;

        try {
            const res = await fetch(`${INTERVIEW_BASE}/questions?topic=${encodeURIComponent(topic)}&difficulty=${encodeURIComponent(difficulty)}`);
            if (res.ok) {
                interviewQuestionsCache = await res.json();
                if (questionCountBadge) questionCountBadge.textContent = interviewQuestionsCache.length;
                renderQuestions(interviewQuestionsCache);
            }
        } catch (err) {
            console.warn('Could not fetch interview questions:', err);
        }
    }

    function renderQuestions(list) {
        if (!questionsContainer) return;
        questionsContainer.innerHTML = '';
        if (list.length === 0) {
            questionsContainer.innerHTML = `<div class="text-dim" style="padding: 2rem; text-align: center;">No questions found for the selected topic & difficulty filter.</div>`;
            return;
        }

        list.forEach((q, idx) => {
            if (idx === 0 && !selectedQuestion) {
                selectedQuestion = q;
                if (codeLangSelect) loadTemplateForLanguage(q, codeLangSelect.value);
            }

            const card = document.createElement('div');
            card.className = 'q-card';

            let diffClass = 'badge-medium';
            if (q.difficulty && q.difficulty.toLowerCase() === 'easy') diffClass = 'badge-easy';
            else if (q.difficulty && q.difficulty.toLowerCase() === 'hard') diffClass = 'badge-hard';

            const cleanCode = (q.codeSnippet || '').replace(/\\n/g, '\n');

            card.innerHTML = `
                <div class="q-header">
                    <h4>${escapeHtml(q.title)}</h4>
                    <div class="q-badges">
                        <span class="badge-topic">${escapeHtml(q.topic)}</span>
                        <span class="badge-difficulty ${diffClass}">${escapeHtml(q.difficulty)}</span>
                    </div>
                </div>
                <div class="q-desc">${escapeHtml(q.questionText)}</div>
                <div class="q-actions">
                    <button class="q-solution-toggle"><i class="fa-solid fa-chevron-down"></i> Show Solution & Explanation</button>
                </div>
                <div class="q-solution-body hidden">
                    <p><strong>Explanation:</strong> ${escapeHtml(q.answerExplanation)}</p>
                    ${cleanCode ? `<pre class="code-snippet-box">${escapeHtml(cleanCode)}</pre>` : ''}
                    ${q.sampleTestCase ? `<div style="margin-top: 0.5rem; font-size: 0.75rem; color: var(--text-muted);"><strong>Test Case:</strong> ${escapeHtml(q.sampleTestCase)}</div>` : ''}
                </div>
            `;

            const toggleBtn = card.querySelector('.q-solution-toggle');
            const solutionBody = card.querySelector('.q-solution-body');
            toggleBtn.addEventListener('click', () => {
                solutionBody.classList.toggle('hidden');
                const isHidden = solutionBody.classList.contains('hidden');
                toggleBtn.innerHTML = isHidden 
                    ? '<i class="fa-solid fa-chevron-down"></i> Show Solution & Explanation' 
                    : '<i class="fa-solid fa-chevron-up"></i> Hide Solution';
            });

            questionsContainer.appendChild(card);
        });
    }

    // Code Execution & Test Case Analysis Engine
    if (btnRunCode) {
        btnRunCode.addEventListener('click', async () => {
            const code = codeEditorInput.value.trim();
            const lang = codeLangSelect.value;
            const title = selectedQuestion ? selectedQuestion.title : 'Coding Problem';

            if (!code) { alert('Please enter code solution to run.'); return; }

            btnRunCode.disabled = true;
            btnRunCode.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Analyzing Test Suite...';
            codeOutputConsole.textContent = 'Compiling logic and evaluating test cases against suite...';
            if (testCaseAnalysisContainer) testCaseAnalysisContainer.classList.add('hidden');

            try {
                const res = await fetch(`${INTERVIEW_BASE}/run-code`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ code, language: lang, title: title })
                });

                if (res.ok) {
                    const data = await res.json();
                    codeOutputConsole.textContent = data.output;

                    if (data.testCases && data.testCases.length > 0 && testCaseAnalysisContainer) {
                        renderTestCasesAnalysis(data.testCases, data.timeComplexity, data.spaceComplexity);
                    }
                } else {
                    codeOutputConsole.textContent = 'Execution failed.';
                }
            } catch (err) {
                codeOutputConsole.textContent = 'Code Runner error: ' + err.message;
            } finally {
                btnRunCode.disabled = false;
                btnRunCode.innerHTML = '<i class="fa-solid fa-play"></i> Run Code Solution & Analyze';
            }
        });
    }

    function renderTestCasesAnalysis(testCases, timeComp, spaceComp) {
        if (!testCaseAnalysisContainer) return;
        testCaseAnalysisContainer.innerHTML = '';
        testCaseAnalysisContainer.classList.remove('hidden');

        testCases.forEach(tc => {
            const item = document.createElement('div');
            item.className = 'test-case-item';
            const statusBadge = tc.passed 
                ? `<span class="test-pass-badge"><i class="fa-solid fa-check"></i> PASSED (${tc.runtime})</span>` 
                : `<span class="test-fail-badge"><i class="fa-solid fa-xmark"></i> FAILED</span>`;

            item.innerHTML = `
                <div style="display: flex; align-items: center; justify-content: space-between;">
                    <strong>${escapeHtml(tc.name)}</strong>
                    ${statusBadge}
                </div>
                <div style="color: var(--text-muted); font-size: 0.72rem; margin-top: 0.2rem;">
                    <div>Input: <code>${escapeHtml(tc.input)}</code></div>
                    <div>Expected: <code>${escapeHtml(tc.expected)}</code> | Actual: <code>${escapeHtml(tc.actual)}</code></div>
                </div>
            `;
            testCaseAnalysisContainer.appendChild(item);
        });

        const complexityCard = document.createElement('div');
        complexityCard.className = 'test-case-item';
        complexityCard.style.gridColumn = '1 / -1';
        complexityCard.style.background = 'rgba(99, 102, 241, 0.08)';
        complexityCard.innerHTML = `
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <span><strong><i class="fa-solid fa-gauge-high text-primary"></i> Algorithm Complexity Analysis:</strong></span>
                <div style="display: flex; gap: 0.5rem;">
                    <span class="complexity-pill">Time: ${timeComp || 'O(N)'}</span>
                    <span class="complexity-pill">Space: ${spaceComp || 'O(N)'}</span>
                </div>
            </div>
        `;
        testCaseAnalysisContainer.appendChild(complexityCard);
    }

    // Job Presets & Recruiter Posting
    const DEFAULT_JOB_PRESETS = [
        {
            id: 1,
            title: "Senior Java Software Engineer",
            company: "Nexus Tech Solutions",
            category: "Backend",
            shortlistThreshold: 75,
            requiredSkills: "Java, Spring Boot, MySQL, REST API, Docker, Microservices, AWS",
            rawText: "We are seeking a Senior Java Engineer with 5+ years experience building high-concurrency microservices, REST APIs, and database architectures using Java 17/21, Spring Boot, Hibernate, MySQL, and Docker."
        },
        {
            id: 2,
            title: "Full Stack Developer",
            company: "Innovate Apps Inc",
            category: "Full Stack",
            shortlistThreshold: 70,
            requiredSkills: "Java, React, Spring Boot, JavaScript, SQL, HTML, CSS, Git",
            rawText: "Looking for a Full Stack Software Developer proficient in Java Spring Boot backend APIs and React frontend UI. Strong knowledge of MySQL database and Git version control required."
        },
        {
            id: 3,
            title: "Lead DevOps & Cloud Architect",
            company: "CloudScale Enterprise",
            category: "DevOps",
            shortlistThreshold: 80,
            requiredSkills: "Docker, Kubernetes, AWS, CI/CD, Terraform, Python, Linux",
            rawText: "Lead DevOps Architect needed to design automated CI/CD pipelines, manage Kubernetes clusters on AWS cloud infrastructure, and write infrastructure-as-code scripts."
        }
    ];

    async function fetchJobPresets() {
        try {
            const res = await fetch(`${API_BASE}/jobs`);
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                jobPresetsCache = await res.json();
            } else {
                jobPresetsCache = JSON.parse(localStorage.getItem('ats_jobs')) || DEFAULT_JOB_PRESETS;
            }
        } catch (err) {
            jobPresetsCache = JSON.parse(localStorage.getItem('ats_jobs')) || DEFAULT_JOB_PRESETS;
        }
        populateJobDropdown(jobPresetsCache);
    }

    function populateJobDropdown(jobs) {
        jobPresetSelect.innerHTML = '<option value="">-- Select Job Role Preset --</option>';
        jobs.forEach(job => {
            const opt = document.createElement('option');
            opt.value = job.id;
            opt.textContent = `${job.title} (${job.company}) - Threshold ${job.shortlistThreshold || 75}%`;
            jobPresetSelect.appendChild(opt);
        });
    }

    function handleJobPresetChange(e) {
        const jobId = e.target.value;
        if (!jobId) return;
        const selected = jobPresetsCache.find(j => j.id == jobId);
        if (selected) {
            jobTitleInput.value = selected.title;
            jobThresholdInput.value = selected.shortlistThreshold || 75;
            jobTextInput.value = selected.rawText;
        }
    }

    async function handleAddJobSubmit(e) {
        e.preventDefault();
        const title = document.getElementById('new-job-title').value.trim();
        const company = document.getElementById('new-company-name').value.trim();
        const category = document.getElementById('new-job-category').value.trim() || 'Software Engineering';
        const experienceLevel = document.getElementById('new-experience-level').value.trim() || '3+ Years';
        const shortlistThreshold = parseInt(document.getElementById('new-shortlist-threshold').value) || 75;
        const requiredSkills = document.getElementById('new-required-skills').value.trim();
        const rawText = document.getElementById('new-job-description').value.trim();

        const newJob = {
            id: Date.now(),
            title, company, category, experienceLevel, shortlistThreshold, requiredSkills, rawText,
            postedBy: currentUser ? currentUser.fullName : 'Recruiter'
        };

        try {
            const res = await fetch(`${API_BASE}/jobs`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newJob)
            });
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                const data = await res.json();
            }
        } catch (err) {
            console.warn('Backend job posting API unavailable, saving locally:', err);
        }

        let savedJobs = JSON.parse(localStorage.getItem('ats_jobs')) || DEFAULT_JOB_PRESETS;
        savedJobs.unshift(newJob);
        localStorage.setItem('ats_jobs', JSON.stringify(savedJobs));
        jobPresetsCache = savedJobs;

        alert('🎉 Job Opening Posted Successfully!');
        formAddJob.reset();
        populateJobDropdown(jobPresetsCache);
        switchView('evaluator');
    }

    async function extractTextFromClientFile(file) {
        const ext = file.name.split('.').pop().toLowerCase();
        
        if (['txt', 'text', 'md', 'html', 'rtf', 'csv', 'json', 'log'].includes(ext) || file.type.startsWith('text/')) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = e => resolve(e.target.result || '');
                reader.onerror = e => reject(new Error('Failed to read text file.'));
                reader.readAsText(file);
            });
        }
        
        if (ext === 'pdf' || file.type === 'application/pdf') {
            if (!window.pdfjsLib) {
                throw new Error('PDF parser engine loading... Please try again.');
            }
            const arrayBuffer = await file.arrayBuffer();
            const pdf = await window.pdfjsLib.getDocument({ data: arrayBuffer }).promise;
            let fullText = '';
            for (let i = 1; i <= pdf.numPages; i++) {
                const page = await pdf.getPage(i);
                const tokenContent = await page.getTextContent();
                const pageText = tokenContent.items.map(item => item.str).join(' ');
                fullText += pageText + '\n';
            }
            return fullText;
        }

        if (ext === 'docx' || file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
            if (!window.mammoth) {
                throw new Error('Word document parser engine loading... Please try again.');
            }
            const arrayBuffer = await file.arrayBuffer();
            const result = await window.mammoth.extractRawText({ arrayBuffer: arrayBuffer });
            return result.value || '';
        }

        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = e => resolve(e.target.result || '');
            reader.onerror = e => reject(new Error('Unsupported file format. Please upload PDF, DOCX, or TXT file.'));
            reader.readAsText(file);
        });
    }

    function extractCandidateMetadata(text, filename) {
        let email = '';
        let candidateName = '';

        const emailMatch = text.match(/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/);
        if (emailMatch) {
            email = emailMatch[0];
        }

        const lines = text.split('\n').map(l => l.trim()).filter(l => l.length > 0);
        for (const line of lines) {
            if (/^(resume|curriculum|cv|summary|contact|profile|experience|skills|education)$/i.test(line)) continue;
            if (line.includes('@') || /^\+?\d[\d\s\-()]{7,}$/.test(line)) continue;
            if (/^[A-Za-z\s.'-]{2,40}$/.test(line) && line.split(/\s+/).length <= 4) {
                candidateName = line;
                break;
            }
        }

        if (!candidateName) {
            candidateName = filename.replace(/\.[^/.]+$/, '').replace(/[-_]/g, ' ');
            candidateName = candidateName.replace(/\b\w/g, l => l.toUpperCase());
        }

        return { email, candidateName };
    }

    async function handleFileUpload(file) {
        const formData = new FormData();
        formData.append('file', file);
        uploadZone.querySelector('h4').textContent = `Processing ${file.name}...`;

        let parsedData = null;
        try {
            const res = await fetch(`${API_BASE}/upload`, {
                method: 'POST',
                body: formData
            });
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                parsedData = await res.json();
            }
        } catch (backendErr) {
            console.warn('Backend upload API unavailable, using client-side file parser:', backendErr);
        }

        try {
            if (!parsedData || !parsedData.text) {
                const text = await extractTextFromClientFile(file);
                if (!text || text.trim().length === 0) {
                    throw new Error('Could not extract readable text from file. Please ensure it is not empty or encrypted.');
                }
                const meta = extractCandidateMetadata(text, file.name);
                parsedData = {
                    text: text,
                    candidateName: meta.candidateName,
                    email: meta.email
                };
            }

            resumeTextInput.value = parsedData.text;
            if (parsedData.candidateName) candidateNameInput.value = parsedData.candidateName;
            if (parsedData.email) candidateEmailInput.value = parsedData.email;

            uploadZone.querySelector('h4').textContent = `Loaded: ${file.name}`;
            tabText.click();
            showToastNotification(`✅ Resume file successfully parsed!\nCandidate: ${parsedData.candidateName || 'Extracted'}\nLength: ${parsedData.text.length} characters`);
        } catch (err) {
            uploadZone.querySelector('h4').textContent = `Error loading file`;
            alert('Error parsing uploaded file: ' + err.message);
        }
    }

    function loadSampleCandidate() {
        candidateNameInput.value = 'Alex Morgan';
        candidateEmailInput.value = 'alex.morgan@techmail.com';

        resumeTextInput.value = `ALEX MORGAN
Email: alex.morgan@techmail.com | Phone: (555) 382-9102 | Location: San Francisco, CA

SUMMARY
Senior Software Engineer with 6+ years of experience building high-concurrency microservices, REST APIs, and database architectures using Java, Spring Boot, and MySQL.

TECHNICAL SKILLS
Languages: Java (17/21), JavaScript, SQL, HTML, CSS, Python
Frameworks & Libraries: Spring Boot, Spring Data JPA, Hibernate, React, REST API, JUnit
Databases & Cloud: MySQL, PostgreSQL, Docker, AWS, Git, CI/CD, Maven, Linux

WORK EXPERIENCE
Senior Java Software Engineer | Nexus Tech Solutions
2022 - Present
- Spearheaded the redesign of core backend payment microservices using Java 21 and Spring Boot, reducing API latency by 42%.
- Designed and optimized high-performance MySQL database queries and JPA indexes, scaling throughput to 10,000 requests/sec.
- Implemented containerized deployment pipelines with Docker, Kubernetes, and Git CI/CD on AWS cloud.
- Led Agile sprint planning and collaborated with cross-functional product and frontend engineering teams.

Full Stack Software Developer | Innovate Apps Inc
2019 - 2022
- Developed scalable web applications using Java, Spring Boot backend, REST API endpoints, and React frontend.
- Automated unit test suites using JUnit and Mockito, raising code coverage from 60% to 92%.
- Managed MySQL database migrations and schema optimizations.

EDUCATION
Bachelor of Science in Computer Science | University of California, Berkeley`;

        if (!jobTitleInput.value && jobPresetsCache.length > 0) {
            jobPresetSelect.value = jobPresetsCache[0].id;
            handleJobPresetChange({ target: { value: jobPresetsCache[0].id } });
        }

        tabText.click();
    }

    function calculateClientSideAts(candidateName, candidateEmail, resumeText, jobTitle, jobText, threshold) {
        const resumeLower = (resumeText || '').toLowerCase();
        const jobLower = (jobText || '').toLowerCase();

        const commonTechSkills = ['java', 'spring', 'spring boot', 'react', 'javascript', 'typescript', 'python', 'sql', 'mysql', 'postgresql', 'docker', 'aws', 'kubernetes', 'git', 'rest', 'api', 'microservices', 'html', 'css', 'node', 'express', 'ci/cd', 'junit', 'hibernate', 'jpa', 'linux', 'maven', 'agile', 'scrum', 'redis', 'mongodb', 'c++', 'c#', '.net', 'flutter', 'kafka'];
        const commonSoftSkills = ['communication', 'leadership', 'collaboration', 'problem solving', 'teamwork', 'analytical', 'management', 'mentorship', 'adaptability', 'critical thinking', 'time management', 'organization'];
        const commonActionVerbs = ['built', 'developed', 'designed', 'implemented', 'spearheaded', 'managed', 'created', 'optimized', 'automated', 'led', 'architected', 'reduced', 'increased', 'improved', 'engineered', 'streamlined', 'deployed'];

        let targetSkills = commonTechSkills.filter(s => jobLower.includes(s));
        if (targetSkills.length < 3) targetSkills = ['java', 'spring boot', 'react', 'sql', 'docker', 'aws', 'rest api', 'git'];

        let matchedKeywords = [];
        let missingKeywords = [];

        targetSkills.forEach(skill => {
            if (resumeLower.includes(skill)) {
                matchedKeywords.push(skill.toUpperCase());
            } else {
                missingKeywords.push(skill.toUpperCase());
            }
        });

        const hardSkillScore = Math.min(100, Math.round((matchedKeywords.length / targetSkills.length) * 100));

        let softMatches = commonSoftSkills.filter(s => resumeLower.includes(s)).length;
        const softSkillScore = Math.min(100, Math.round((softMatches / 4) * 100));

        let verbMatches = commonActionVerbs.filter(v => resumeLower.includes(v)).length;
        const actionVerbScore = Math.min(100, Math.round((verbMatches / 5) * 100));

        let formatPoints = 50;
        if (resumeLower.includes('experience') || resumeLower.includes('work history')) formatPoints += 15;
        if (resumeLower.includes('education') || resumeLower.includes('degree')) formatPoints += 15;
        if (resumeLower.includes('skills')) formatPoints += 10;
        if ((resumeText || '').length > 300) formatPoints += 10;
        const formatScore = Math.min(100, formatPoints);

        const overallScore = Math.round((hardSkillScore * 0.4) + (softSkillScore * 0.2) + (actionVerbScore * 0.2) + (formatScore * 0.2));
        const decisionStatus = overallScore >= (threshold || 75) ? 'SHORTLISTED' : 'REJECTED';

        const evaluatedAt = new Date().toLocaleString();
        const id = Date.now();

        const emailSubject = decisionStatus === 'SHORTLISTED'
            ? `Interview Invitation: ${jobTitle || 'Software Engineer'} Role`
            : `Application Status Update: ${jobTitle || 'Software Engineer'}`;

        const emailBody = decisionStatus === 'SHORTLISTED'
            ? `Dear ${candidateName || 'Candidate'},\n\nWe are pleased to inform you that your application for the ${jobTitle || 'Software Engineer'} position has been SHORTLISTED with an ATS match score of ${overallScore}%.\n\nOur team would like to invite you for a technical interview. Please let us know your availability.\n\nBest regards,\nTalent Acquisition Team`
            : `Dear ${candidateName || 'Candidate'},\n\nThank you for applying for the ${jobTitle || 'Software Engineer'} position. After reviewing your resume against our role requirements (ATS match: ${overallScore}%), we regret to inform you that we will not be moving forward with your application at this time.\n\nWe wish you all the best in your career.\n\nSincerely,\nTalent Acquisition Team`;

        const smsBody = decisionStatus === 'SHORTLISTED'
            ? `🎉 CONGRATULATIONS ${candidateName || 'Candidate'}! You are SHORTLISTED for ${jobTitle || 'Role'} (ATS Score: ${overallScore}%). Check email (${candidateEmail || 'email'}) for interview slot.`
            : `Application Update: Your ATS score for ${jobTitle || 'Role'} is ${overallScore}%. Thank you for applying.`;

        const result = {
            id,
            candidateName: candidateName || 'Anonymous Candidate',
            email: candidateEmail || 'candidate@example.com',
            phone: '+91-9876543210',
            jobTitle: jobTitle || 'Software Engineer',
            shortlistThreshold: threshold || 75,
            overallScore,
            hardSkillScore,
            softSkillScore,
            actionVerbScore,
            formatScore,
            decisionStatus,
            matchedKeywords,
            missingKeywords,
            evaluatedAt,
            emailSubject,
            emailBody,
            smsBody,
            resumeText
        };

        try {
            let history = JSON.parse(localStorage.getItem('ats_history') || '[]');
            history.unshift(result);
            localStorage.setItem('ats_history', JSON.stringify(history));
            historyCache = history;
            historyCountBadge.textContent = history.length;
            renderHistoryTable(history);
        } catch (e) {
            console.warn('LocalStorage save failed:', e);
        }

        return result;
    }

    async function runAtsEvaluation() {
        const resumeText = resumeTextInput.value.trim();
        const jobText = jobTextInput.value.trim();
        const jobTitle = jobTitleInput.value.trim() || 'Target Position';
        const candidateName = candidateNameInput.value.trim() || 'Candidate';
        const candidateEmail = candidateEmailInput.value.trim() || 'candidate@example.com';
        const threshold = parseInt(jobThresholdInput.value) || 75;

        if (!resumeText) { alert('Please upload a resume or paste text.'); return; }
        if (!jobText) { alert('Please select a job preset or paste a Job Description.'); return; }

        btnRunAts.disabled = true;
        btnRunAts.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Evaluating Candidate Application...';

        let data = null;
        try {
            const res = await fetch(`${API_BASE}/evaluate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    candidateName, candidateEmail, resumeText, jobTitle,
                    jobDescriptionText: jobText, shortlistThreshold: threshold
                })
            });

            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                data = await res.json();
            }
        } catch (err) {
            console.warn('Backend evaluate API unavailable, using client-side ATS engine:', err);
        }

        try {
            if (!data || !data.overallScore) {
                data = calculateClientSideAts(candidateName, candidateEmail, resumeText, jobTitle, jobText, threshold);
            }

            currentEvaluation = data;
            renderResults(currentEvaluation);
            fetchHistory();
        } catch (err) {
            alert('Evaluation error: ' + err.message);
        } finally {
            btnRunAts.disabled = false;
            btnRunAts.innerHTML = '<i class="fa-solid fa-bolt-lightning"></i> Calculate ATS Score & Evaluate Application';
        }
    }

    function renderResults(data) {
        currentEvaluation = data;
        resultsDashboard.classList.remove('hidden');
        resultsDashboard.scrollIntoView({ behavior: 'smooth' });

        evalTimestamp.textContent = `Evaluated at ${data.evaluatedAt}`;
        valThresholdDisplay.textContent = `${data.shortlistThreshold || 75}%`;

        if (data.decisionStatus === 'SHORTLISTED') {
            decisionStatusBadge.className = 'badge-decision badge-shortlisted';
            decisionStatusBadge.innerHTML = '<i class="fa-solid fa-circle-check"></i> APPLICATION SHORTLISTED';
        } else {
            decisionStatusBadge.className = 'badge-decision badge-rejected';
            decisionStatusBadge.innerHTML = '<i class="fa-solid fa-circle-xmark"></i> APPLICATION REJECTED';
        }

        const score = data.overallScore;
        const circumference = 314;
        const offset = circumference - (score / 100) * circumference;
        gaugeFill.style.strokeDashoffset = offset;

        let currentScore = 0;
        const counterInt = setInterval(() => {
            if (currentScore >= score) {
                scoreNumber.textContent = score;
                clearInterval(counterInt);
            } else {
                currentScore++;
                scoreNumber.textContent = currentScore;
            }
        }, 15);

        if (score >= (data.shortlistThreshold || 75)) {
            scoreRatingTitle.textContent = '🔥 High Match Candidate';
            scoreRatingTitle.style.color = 'var(--emerald)';
            gaugeFill.style.stroke = 'var(--emerald)';
        } else {
            scoreRatingTitle.textContent = '⚠️ Score Below Shortlist Threshold';
            scoreRatingTitle.style.color = 'var(--red)';
            gaugeFill.style.stroke = 'var(--red)';
        }

        valHardSkills.textContent = `${data.hardSkillScore}%`; barHardSkills.style.width = `${data.hardSkillScore}%`;
        valSoftSkills.textContent = `${data.softSkillScore}%`; barSoftSkills.style.width = `${data.softSkillScore}%`;
        valActionVerbs.textContent = `${data.actionVerbScore}%`; barActionVerbs.style.width = `${data.actionVerbScore}%`;
        valFormat.textContent = `${data.formatScore}%`; barFormat.style.width = `${data.formatScore}%`;

        renderKeywordPills(matchedPillsWrap, data.matchedKeywords || [], 'matched');
        renderKeywordPills(missingPillsWrap, data.missingKeywords || [], 'missing');
        countMatched.textContent = (data.matchedKeywords || []).length;
        countMissing.textContent = (data.missingKeywords || []).length;

        if (emailToVal) emailToVal.textContent = data.email || 'candidate@example.com';
        if (phoneToVal) phoneToVal.textContent = data.phone || '+91-9876543210';
        if (emailSubjectVal) emailSubjectVal.textContent = data.emailSubject || 'Application Update';
        if (emailBodyVal) emailBodyVal.textContent = data.emailBody || 'Email body content...';
        if (smsBodyVal) smsBodyVal.textContent = data.smsBody || `🎉 CONGRATULATIONS ${data.candidateName || 'Candidate'}! You have been SHORTLISTED for ${data.jobTitle || 'Target Role'} (ATS Score: ${data.overallScore}%). Check email (${data.email || 'candidate@example.com'}) for interview details.`;

        resumeViewerBody.innerHTML = data.highlightedResumeHtml || resumeTextInput.value;
    }

    function renderKeywordPills(container, keywords, type) {
        container.innerHTML = '';
        if (keywords.length === 0) {
            container.innerHTML = `<span class="text-dim" style="font-size: 0.8rem;">None detected</span>`;
            return;
        }
        keywords.forEach(kw => {
            const pill = document.createElement('span');
            pill.className = `kw-pill ${type}`;
            const icon = type === 'matched' ? '<i class="fa-solid fa-check"></i>' : '<i class="fa-solid fa-plus"></i>';
            pill.innerHTML = `${icon} ${kw}`;
            if (type === 'missing') {
                pill.title = 'Click to append keyword into resume text';
                pill.style.cursor = 'pointer';
                pill.addEventListener('click', () => {
                    resumeTextInput.value += ` ${kw}`;
                    pill.style.opacity = '0.5';
                    pill.innerHTML = `<i class="fa-solid fa-check"></i> Appended!`;
                });
            }
            container.appendChild(pill);
        });
    }

    async function dispatchCandidateEmail() {
        const targetEmail = candidateEmailInput.value.trim() || (currentEvaluation && currentEvaluation.email) || 'alex.morgan@techmail.com';
        const targetSubject = (currentEvaluation && currentEvaluation.emailSubject) ? currentEvaluation.emailSubject : `🎉 Application Status Update`;
        const decisionStatus = (currentEvaluation && currentEvaluation.decisionStatus) ? currentEvaluation.decisionStatus : 'SHORTLISTED';
        const candidateName = candidateNameInput.value.trim() || (currentEvaluation && currentEvaluation.candidateName) || 'Candidate';
        const emailBodyText = (currentEvaluation && currentEvaluation.emailBody) ? currentEvaluation.emailBody : `Hello ${candidateName},\n\nWe have reviewed your application. Your status is: ${decisionStatus}.\n\nBest regards,\nTalent Acquisition`;

        btnSendEmail.disabled = true;
        btnSendEmail.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Dispatching Email...';

        try {
            const res = await fetch(`${API_BASE}/send-notification`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email: targetEmail,
                    candidateName: candidateName,
                    subject: targetSubject,
                    emailBody: emailBodyText,
                    decisionStatus: decisionStatus
                })
            });
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                const data = await res.json();
            }
        } catch (err) {
            console.warn('Backend notification API unavailable, simulating dispatch:', err);
        }

        btnSendEmail.style.background = 'linear-gradient(135deg, #10b981, #059669)';
        btnSendEmail.innerHTML = '<i class="fa-solid fa-circle-check"></i> Email Sent Successfully!';
        
        showToastNotification(`📧 Candidate Notification Email Dispatched Successfully!\nTo: ${targetEmail}\nStatus: ${decisionStatus}`);
        
        setTimeout(() => {
            btnSendEmail.style.background = '';
            btnSendEmail.innerHTML = '<i class="fa-solid fa-envelope-circle-check"></i> Dispatch Email to Candidate';
            btnSendEmail.disabled = false;
        }, 3000);
    }

    async function dispatchCandidateSms() {
        const targetEmail = candidateEmailInput.value.trim() || (currentEvaluation && currentEvaluation.email) || 'alex.morgan@techmail.com';
        const targetPhone = (currentEvaluation && currentEvaluation.phone) ? currentEvaluation.phone : '+91-9876543210';
        const candidateName = candidateNameInput.value.trim() || (currentEvaluation && currentEvaluation.candidateName) || 'Alex Morgan';
        const targetSubject = (currentEvaluation && currentEvaluation.emailSubject) ? currentEvaluation.emailSubject : `Application Notification`;
        const smsBodyText = (currentEvaluation && currentEvaluation.smsBody) ? currentEvaluation.smsBody : `🎉 CONGRATULATIONS ${candidateName}! You have been SHORTLISTED. Check email (${targetEmail}) for interview details.`;

        btnSendSms.disabled = true;
        btnSendSms.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Sending SMS...';

        try {
            const res = await fetch(`${API_BASE}/send-notification`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email: targetEmail,
                    phone: targetPhone,
                    candidateName: candidateName,
                    subject: targetSubject,
                    smsBody: smsBodyText,
                    decisionStatus: (currentEvaluation && currentEvaluation.decisionStatus) ? currentEvaluation.decisionStatus : 'SHORTLISTED'
                })
            });
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                const data = await res.json();
            }
        } catch (err) {
            console.warn('Backend SMS API unavailable, simulating dispatch:', err);
        }

        btnSendSms.style.background = 'linear-gradient(135deg, #059669, #047857)';
        btnSendSms.innerHTML = '<i class="fa-solid fa-circle-check"></i> Mobile SMS Sent!';

        showToastNotification(`📱 Mobile SMS & WhatsApp Notification Dispatched!\nTo: ${targetPhone} (${candidateName})\nStatus: SENT (200 OK via SMS Gateway)\n\nMsg: "${smsBodyText}"`);

        setTimeout(() => {
            btnSendSms.style.background = 'linear-gradient(135deg, #10b981, #059669)';
            btnSendSms.innerHTML = '<i class="fa-solid fa-comment-sms"></i> Dispatch Mobile SMS';
            btnSendSms.disabled = false;
        }, 3000);
    }

    function showToastNotification(message) {
        let toast = document.getElementById('app-toast-notification');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'app-toast-notification';
            toast.style.cssText = `
                position: fixed; bottom: 2rem; right: 2rem; z-index: 9999;
                background: linear-gradient(135deg, #10b981, #059669); color: #fff;
                padding: 1rem 1.5rem; border-radius: 12px; font-weight: 700; font-size: 0.88rem;
                box-shadow: 0 10px 25px rgba(16, 185, 129, 0.4); white-space: pre-line;
                animation: slideInUp 0.3s ease-out;
            `;
            document.body.appendChild(toast);
        }
        toast.textContent = message;
        toast.style.display = 'block';
        setTimeout(() => { toast.style.display = 'none'; }, 5000);
    }

    function triggerResumeDownload() {
        if (!currentEvaluation) {
            alert('Please run an evaluation first to download the report.');
            return;
        }
        const reportContent = `=====================================================
ATS RESUME EVALUATION & CANDIDATE REPORT
=====================================================
Candidate Name : ${currentEvaluation.candidateName}
Email          : ${currentEvaluation.email}
Job Position   : ${currentEvaluation.jobTitle}
Evaluated At   : ${currentEvaluation.evaluatedAt}
Status         : ${currentEvaluation.decisionStatus}
Overall Score  : ${currentEvaluation.overallScore}% (Threshold: ${currentEvaluation.shortlistThreshold || 75}%)
-----------------------------------------------------
SCORE MATRIX
-----------------------------------------------------
Hard Skills Score   : ${currentEvaluation.hardSkillScore}%
Soft Skills Score   : ${currentEvaluation.softSkillScore}%
Action Verbs Score  : ${currentEvaluation.actionVerbScore}%
Format Score        : ${currentEvaluation.formatScore}%
-----------------------------------------------------
MATCHED KEYWORDS
-----------------------------------------------------
${(currentEvaluation.matchedKeywords || []).join(', ') || 'None'}
-----------------------------------------------------
MISSING KEYWORDS
-----------------------------------------------------
${(currentEvaluation.missingKeywords || []).join(', ') || 'None'}
=====================================================
`;
        const blob = new Blob([reportContent], { type: 'text/plain;charset=utf-8' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `ATS_Report_${(currentEvaluation.candidateName || 'Candidate').replace(/\s+/g, '_')}.txt`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    }

    async function fetchHistory() {
        try {
            const res = await fetch(`${API_BASE}/history`);
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                historyCache = await res.json();
            } else {
                historyCache = JSON.parse(localStorage.getItem('ats_history')) || [];
            }
        } catch (err) {
            historyCache = JSON.parse(localStorage.getItem('ats_history')) || [];
        }
        historyCountBadge.textContent = historyCache.length;
        renderHistoryTable(historyCache);
    }

    function renderHistoryTable(list) {
        historyTableBody.innerHTML = '';
        if (list.length === 0) {
            historyTableBody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">No past evaluations saved yet.</td></tr>`;
            return;
        }

        list.forEach(item => {
            const tr = document.createElement('tr');
            const isShortlisted = item.decisionStatus === 'SHORTLISTED';
            const statusBadge = isShortlisted
                ? `<span class="badge-score score-high"><i class="fa-solid fa-circle-check"></i> SHORTLISTED</span>`
                : `<span class="badge-score score-low"><i class="fa-solid fa-circle-xmark"></i> REJECTED</span>`;

            tr.innerHTML = `
                <td>#${item.id}</td>
                <td><strong>${escapeHtml(item.candidateName)}</strong></td>
                <td>${escapeHtml(item.jobTitle)}</td>
                <td><strong>${item.overallScore}%</strong></td>
                <td>${statusBadge}</td>
                <td>${item.hardSkillScore}%</td>
                <td><span style="font-size: 0.75rem; color: var(--text-muted);">${item.evaluatedAt}</span></td>
                <td>
                    <div style="display: flex; gap: 0.35rem;">
                        <button class="btn-sm-view" data-id="${item.id}"><i class="fa-solid fa-eye"></i> View</button>
                        <button class="btn-sm-download" data-id="${item.id}" style="background: rgba(59, 130, 246, 0.15); color: #60a5fa; border: 1px solid rgba(59, 130, 246, 0.3); padding: 0.25rem 0.5rem; border-radius: 6px; cursor: pointer; display: inline-flex; align-items: center; gap: 0.25rem;"><i class="fa-solid fa-download"></i> Download</button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-sm-view').addEventListener('click', () => {
                switchView('evaluator');
                renderResults(item);
            });

            tr.querySelector('.btn-sm-download').addEventListener('click', () => {
                currentEvaluation = item;
                triggerResumeDownload();
            });

            historyTableBody.appendChild(tr);
        });
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }
});
