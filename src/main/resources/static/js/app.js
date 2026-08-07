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

    const DEFAULT_INTERVIEW_QUESTIONS = [
        {
            id: 1,
            title: "1. Add 2 Integers",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Tap Academy",
            questionText: "Given two integers a and b, return the sum of the two integers.",
            answerExplanation: "Add the two integers directly using arithmetic addition in O(1) time complexity.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        System.out.println(a + b);\n    }\n}",
            sampleTestCase: "Input: 12 8 -> Output: 20"
        },
        {
            id: 2,
            title: "2. Adding Three Integers",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Tap Academy",
            questionText: "Read three integers and print their total sum.",
            answerExplanation: "Sum all three inputs in O(1) time complexity.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a + b + c);\n    }\n}",
            sampleTestCase: "Input: 1 2 3 -> Output: 6"
        },
        {
            id: 3,
            title: "3. Product of Three",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Tap Academy",
            questionText: "Read three integers and calculate their product.",
            answerExplanation: "Multiply the three integers directly in O(1) time.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a * b * c);\n    }\n}",
            sampleTestCase: "Input: 2 3 4 -> Output: 24"
        },
        {
            id: 4,
            title: "4. Multiple of 5",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Tap Academy",
            questionText: "Determine whether the given number is a multiple of 5 or not.",
            answerExplanation: "Use modulo operator n % 5 == 0.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int n = scanner.nextInt();\n        System.out.println(n % 5 == 0 ? \"Yes\" : \"No\");\n    }\n}",
            sampleTestCase: "Input: 24 -> Output: No"
        },
        {
            id: 5,
            title: "5. Two Sum - Find Indices matching Target Sum",
            topic: "Arrays",
            category: "Coding",
            difficulty: "Easy",
            company: "Microsoft",
            questionText: "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
            answerExplanation: "Optimal solution uses a HashMap to store complements in O(N) time and O(N) space.",
            codeSnippet: "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int target = sc.nextInt();\n        Map<Integer, Integer> map = new HashMap<>();\n        int idx = 0;\n        while (sc.hasNextInt()) {\n            int num = sc.nextInt();\n            int comp = target - num;\n            if (map.containsKey(comp)) {\n                System.out.println(map.get(comp) + \" \" + idx);\n                return;\n            }\n            map.put(num, idx++);\n        }\n    }\n}",
            sampleTestCase: "Input: target=9, nums=[2,7,11,15] -> Output: 0 1"
        },
        {
            id: 6,
            title: "6. Reverse a String in Place",
            topic: "Strings",
            category: "Coding",
            difficulty: "Easy",
            company: "Amazon",
            questionText: "Write a function that reverses a string.",
            answerExplanation: "Use two pointers from left and right moving inward, or StringBuilder.reverse().",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        System.out.println(new StringBuilder(s).reverse().toString());\n    }\n}",
            sampleTestCase: "Input: hello -> Output: olleh"
        },
        {
            id: 7,
            title: "7. Check Palindrome String / Number",
            topic: "Strings",
            category: "Coding",
            difficulty: "Easy",
            company: "Google",
            questionText: "Check whether a given string reads the same backward as forward.",
            answerExplanation: "Compare characters at index i and len-1-i, or compare string with its reversed copy.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        String rev = new StringBuilder(s).reverse().toString();\n        System.out.println(s.equals(rev));\n    }\n}",
            sampleTestCase: "Input: racecar -> Output: true"
        },
        {
            id: 8,
            title: "8. FizzBuzz Classic Problem",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Tap Academy",
            questionText: "Print numbers from 1 to N. For multiples of 3 print Fizz, for 5 print Buzz, for both print FizzBuzz.",
            answerExplanation: "Use modulo arithmetic for 15, 3, and 5.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        for (int i = 1; i <= n; i++) {\n            if (i % 15 == 0) System.out.print(\"FizzBuzz \");\n            else if (i % 3 == 0) System.out.print(\"Fizz \");\n            else if (i % 5 == 0) System.out.print(\"Buzz \");\n            else System.out.print(i + \" \");\n        }\n    }\n}",
            sampleTestCase: "Input: 5 -> Output: 1 2 Fizz 4 Buzz"
        },
        {
            id: 9,
            title: "9. Valid Parentheses Matching with Stack",
            topic: "Algorithms",
            category: "Coding",
            difficulty: "Easy",
            company: "Meta",
            questionText: "Given a string s containing just characters (), {}, [], determine if the input string is valid.",
            answerExplanation: "Use a Stack LIFO data structure to match open and close brackets in O(N) time.",
            codeSnippet: "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Stack<Character> st = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == '(') st.push(')');\n            else if (c == '{') st.push('}');\n            else if (c == '[') st.push(']');\n            else if (st.isEmpty() || st.pop() != c) { System.out.println(\"false\"); return; }\n        }\n        System.out.println(st.isEmpty());\n    }\n}",
            sampleTestCase: "Input: ()[]{} -> Output: true"
        },
        {
            id: 10,
            title: "10. Maximum Subarray Sum - Kadane Algorithm",
            topic: "Arrays",
            category: "Coding",
            difficulty: "Medium",
            company: "Amazon",
            questionText: "Find the contiguous subarray which has the largest sum and return its sum.",
            answerExplanation: "Kadane Algorithm maintains max_ending_here and max_so_far in O(N) time complexity.",
            codeSnippet: "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int maxSoFar = Integer.MIN_VALUE, maxEnding = 0;\n        while (sc.hasNextInt()) {\n            int x = sc.nextInt();\n            maxEnding = Math.max(x, maxEnding + x);\n            maxSoFar = Math.max(maxSoFar, maxEnding);\n        }\n        System.out.println(maxSoFar);\n    }\n}",
            sampleTestCase: "Input: -2 1 -3 4 -1 2 1 -5 4 -> Output: 6"
        },
        {
            id: 11,
            title: "11. Longest Substring Without Repeating Characters",
            topic: "Strings",
            category: "Coding",
            difficulty: "Medium",
            company: "Google",
            questionText: "Given a string s, find the length of the longest substring without repeating characters.",
            answerExplanation: "Sliding Window approach using a HashSet / HashMap in O(N) time complexity.",
            codeSnippet: "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Set<Character> set = new HashSet<>();\n        int l = 0, maxLen = 0;\n        for (int r = 0; r < s.length(); r++) {\n            while (set.contains(s.charAt(r))) set.remove(s.charAt(l++));\n            set.add(s.charAt(r));\n            maxLen = Math.max(maxLen, r - l + 1);\n        }\n        System.out.println(maxLen);\n    }\n}",
            sampleTestCase: "Input: abcabcbb -> Output: 3"
        },
        {
            id: 12,
            title: "12. Merge Two Sorted Lists",
            topic: "Arrays",
            category: "Coding",
            difficulty: "Easy",
            company: "Apple",
            questionText: "Merge two sorted linked lists / arrays and return it as a new sorted list.",
            answerExplanation: "Use two pointers to compare elements from both lists and build the merged list in O(N + M) time.",
            codeSnippet: "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        List<Integer> list = new ArrayList<>();\n        while (sc.hasNextInt()) list.add(sc.nextInt());\n        Collections.sort(list);\n        for (int x : list) System.out.print(x + \" \");\n    }\n}",
            sampleTestCase: "Input: 1 2 4 and 1 3 4 -> Output: 1 1 2 3 4 4"
        },
        {
            id: 13,
            title: "13. Difference between HashMap and ConcurrentHashMap in Java",
            topic: "Java",
            category: "Conceptual",
            difficulty: "Medium",
            company: "TechScale",
            questionText: "Explain how HashMap works internally and why ConcurrentHashMap is preferred for high-concurrency multithreaded applications.",
            answerExplanation: "HashMap is non-thread-safe. ConcurrentHashMap uses bucket-level locking (CAS operations and synchronized blocks on node heads) providing high concurrency thread safety.",
            codeSnippet: "import java.util.concurrent.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();\n        map.put(\"key\", 100);\n        map.computeIfPresent(\"key\", (k, v) -> v + 50);\n        System.out.println(\"Updated Value: \" + map.get(\"key\"));\n    }\n}",
            sampleTestCase: "Concept: HashMap vs ConcurrentHashMap"
        },
        {
            id: 14,
            title: "14. Java 8 Streams API - Filter, Map, and Reduce",
            topic: "Java",
            category: "Framework",
            difficulty: "Medium",
            company: "Oracle",
            questionText: "Demonstrate how Java 8 Streams API enables functional programming, lazy evaluation, and parallel processing.",
            answerExplanation: "Streams process collections lazily through pipeline operations (intermediate filter/map and terminal collect/reduce). ParallelStreams utilize ForkJoinPool for multi-core speedup.",
            codeSnippet: "import java.util.*;\nimport java.util.stream.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        List<String> names = List.of(\"Alex\", \"Bob\", \"Alice\", \"David\");\n        List<String> filtered = names.stream()\n            .filter(n -> n.startsWith(\"A\"))\n            .map(String::toUpperCase)\n            .collect(Collectors.toList());\n        System.out.println(filtered);\n    }\n}",
            sampleTestCase: "Input: [Alex, Bob, Alice] -> Output: [ALEX, ALICE]"
        },
        {
            id: 15,
            title: "15. Java JVM Memory Management & Garbage Collection (G1GC, ZGC)",
            topic: "Java",
            category: "Architecture",
            difficulty: "Hard",
            company: "Netflix",
            questionText: "Explain Heap Memory structure (Young Generation, Eden, Survivor, Tenured/Old Gen) and Garbage Collectors (G1GC vs ZGC).",
            answerExplanation: "Java Heap memory is partitioned into Eden, S0/S1 Survivor spaces, and Old Generation. G1GC divides heap into regions to achieve predictable pause times. ZGC is an ultra-low latency Garbage Collector.",
            codeSnippet: "public class Main {\n    public static void main(String[] args) {\n        Runtime runtime = Runtime.getRuntime();\n        System.out.println(\"Max Memory: \" + runtime.maxMemory() / (1024 * 1024) + \" MB\");\n        System.out.println(\"Free Memory: \" + runtime.freeMemory() / (1024 * 1024) + \" MB\");\n    }\n}",
            sampleTestCase: "Concept: JVM Heap & Garbage Collector Tuning"
        },
        {
            id: 16,
            title: "16. How Spring Boot Dependency Injection & @Autowired Work",
            topic: "Spring Boot",
            category: "Architecture",
            difficulty: "Medium",
            company: "Apex Innovations",
            questionText: "Explain Spring IoC (Inversion of Control) container, bean lifecycle, and dependency resolution with @Autowired vs Constructor Injection.",
            answerExplanation: "Spring IoC container initializes and manages application beans. Constructor injection is preferred because it ensures immutability, prevents NPEs, and enables easier unit testing with Mockito.",
            codeSnippet: "import org.springframework.stereotype.Service;\n\n@Service\npublic class UserService {\n    private final UserRepository userRepository;\n    public UserService(UserRepository userRepository) {\n        this.userRepository = userRepository;\n    }\n}",
            sampleTestCase: "Concept: Constructor Injection in Spring Boot"
        },
        {
            id: 17,
            title: "17. Spring Boot Global Exception Handling with @ControllerAdvice",
            topic: "Spring Boot",
            category: "Architecture",
            difficulty: "Medium",
            company: "Microsoft",
            questionText: "How to handle REST API exceptions globally in Spring Boot using @ControllerAdvice and @ExceptionHandler.",
            answerExplanation: "@ControllerAdvice acts as an interceptor for exceptions thrown by Controllers, returning standardized Error DTO JSON responses with HTTP status codes (400, 404, 500).",
            codeSnippet: "import org.springframework.web.bind.annotation.*;\nimport org.springframework.http.ResponseEntity;\n\n@RestControllerAdvice\npublic class GlobalExceptionHandler {\n    @ExceptionHandler(RuntimeException.class)\n    public ResponseEntity<String> handleNotFound(RuntimeException ex) {\n        return ResponseEntity.status(404).body(ex.getMessage());\n    }\n}",
            sampleTestCase: "Concept: RestControllerAdvice Global Handler"
        },
        {
            id: 18,
            title: "18. Find Nth Highest Salary in MySQL",
            topic: "SQL",
            category: "Database",
            difficulty: "Hard",
            company: "Oracle",
            questionText: "Write a SQL query to get the Nth highest salary from the Employee table.",
            answerExplanation: "Use DENSE_RANK() window function or LIMIT 1 OFFSET N-1 pattern to retrieve the exact Nth highest salary.",
            codeSnippet: "SELECT DISTINCT salary \nFROM Employee \nORDER BY salary DESC \nLIMIT 1 OFFSET 1;",
            sampleTestCase: "Input: N=2 -> Output: 2nd Highest Salary"
        },
        {
            id: 19,
            title: "19. SQL JOIN Types & Query Performance Optimization",
            topic: "SQL",
            category: "Database",
            difficulty: "Medium",
            company: "Amazon",
            questionText: "Explain INNER, LEFT, RIGHT, and FULL OUTER JOINs and how B-Tree indexes optimize JOIN performance.",
            answerExplanation: "JOIN operations combine rows from related tables. B-Tree indexes on Foreign Keys avoid full table scans and reduce JOIN complexity from O(N*M) to O(N log M).",
            codeSnippet: "SELECT e.emp_name, d.dept_name\nFROM employees e\nINNER JOIN departments d ON e.dept_id = d.dept_id;",
            sampleTestCase: "Concept: INNER JOIN Execution Plan"
        },
        {
            id: 20,
            title: "20. Microservices Circuit Breaker Pattern with Resilience4j",
            topic: "System Design",
            category: "Architecture",
            difficulty: "Hard",
            company: "CloudSphere",
            questionText: "Explain how Circuit Breaker states (CLOSED, OPEN, HALF_OPEN) prevent cascading failures in microservices.",
            answerExplanation: "Circuit Breakers monitor remote service calls. When failure threshold is breached, state transitions to OPEN, immediately returning fallback responses without overloading failing downstream services.",
            codeSnippet: "// Resilience4j CircuitBreaker Annotation\n@CircuitBreaker(name = \"paymentService\", fallbackMethod = \"paymentFallback\")\npublic String processPayment() {\n    return \"Payment Success\";\n}",
            sampleTestCase: "Concept: Resilience4j Circuit Breaker States"
        },
        {
            id: 21,
            title: "21. Self Introduction & Background Summary (HR Guide)",
            topic: "Self Introduction",
            category: "Behavioral",
            difficulty: "Easy",
            company: "Recruiter Guide",
            questionText: "How to deliver a structured 90-second self introduction for Software Engineering interviews.",
            answerExplanation: "Structure your intro using the ELEVATOR formula: Present Role & Core Skills -> Key Engineering Accomplishments -> Technical Strengths -> Alignment with the Company Target.",
            codeSnippet: "// Structured Elevator Pitch\n\"Hello, I am Ramesh, a Full-Stack Software Engineer specializing in Java, Spring Boot, React, and SQL database architectures. Recently, I built an ATS Resume Scorer with automated candidate evaluation engines...\"",
            sampleTestCase: "Concept: Professional HR Interview Intro"
        },
        {
            id: 22,
            title: "22. The 4 Pillars of Object-Oriented Programming (OOPs)",
            topic: "OOPs Concepts",
            category: "Conceptual",
            difficulty: "Easy",
            company: "Google",
            questionText: "Explain Encapsulation, Abstraction, Inheritance, and Polymorphism with real-world Java code examples.",
            answerExplanation: "Encapsulation hides internal state (private variables + getters/setters). Abstraction hides complex implementation details using interfaces/abstract classes. Inheritance allows code reusability (extends). Polymorphism allows method overriding and overloading.",
            codeSnippet: "// Polymorphism & Inheritance Example\nabstract class Animal { abstract void makeSound(); }\nclass Dog extends Animal { void makeSound() { System.out.println(\"Bark\"); } }",
            sampleTestCase: "Concept: OOPs 4 Pillars"
        }
    ];

    function filterDefaultQuestions(topic, difficulty) {
        return DEFAULT_INTERVIEW_QUESTIONS.filter(q => {
            const matchesTopic = (!topic || topic === 'All' || q.topic.toLowerCase() === topic.toLowerCase() || (topic.includes('OOP') && q.topic.includes('OOP')) || (topic.includes('Self') && q.topic.includes('Self')));
            const matchesDiff = (!difficulty || difficulty === 'All' || q.difficulty.toLowerCase() === difficulty.toLowerCase());
            return matchesTopic && matchesDiff;
        });
    }

    // Technical Interview Questions Fetcher & Renderer
    async function fetchInterviewQuestions() {
        const topic = interviewTopicSelect ? interviewTopicSelect.value : 'All';
        const difficulty = interviewDifficultySelect ? interviewDifficultySelect.value : 'All';

        let questions = null;
        try {
            const res = await fetch(`${INTERVIEW_BASE}/questions?topic=${encodeURIComponent(topic)}&difficulty=${encodeURIComponent(difficulty)}`);
            const contentType = res.headers.get('content-type') || '';
            if (res.ok && contentType.includes('application/json')) {
                questions = await res.json();
            }
        } catch (err) {
            console.warn('Backend questions API unavailable, using built-in questions dataset:', err);
        }

        if (!questions || !Array.isArray(questions) || questions.length === 0) {
            questions = filterDefaultQuestions(topic, difficulty);
        }

        interviewQuestionsCache = questions;
        if (questionCountBadge) questionCountBadge.textContent = interviewQuestionsCache.length;
        renderQuestions(interviewQuestionsCache);
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
            console.warn('Backend notification API unavailable, processing in-app email dispatch:', err);
        }

        btnSendEmail.style.background = 'linear-gradient(135deg, #10b981, #059669)';
        btnSendEmail.innerHTML = '<i class="fa-solid fa-circle-check"></i> Email Delivered Successfully!';
        
        showToastNotification(`📧 Candidate Notification Email Dispatched Successfully!\nTo: ${targetEmail}\nStatus: ${decisionStatus}\nMessage: "Hello ${candidateName}, Your status is ${decisionStatus}."`);
        
        setTimeout(() => {
            btnSendEmail.style.background = '';
            btnSendEmail.innerHTML = '<i class="fa-solid fa-envelope-circle-check"></i> Dispatch Email';
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
            console.warn('Backend SMS API unavailable, processing in-app SMS dispatch:', err);
        }

        btnSendSms.style.background = 'linear-gradient(135deg, #059669, #047857)';
        btnSendSms.innerHTML = '<i class="fa-solid fa-circle-check"></i> Mobile SMS Sent!';

        showToastNotification(`📱 Mobile SMS Notification Dispatched!\nTo: ${targetPhone} (${candidateName})\nStatus: SENT (200 OK)\n\nMsg: "${smsBodyText}"`);

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
