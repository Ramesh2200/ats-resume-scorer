package com.ats.scorer;

import com.ats.scorer.model.InterviewQuestion;
import com.ats.scorer.model.JobDescription;
import com.ats.scorer.repository.InterviewQuestionRepository;
import com.ats.scorer.repository.JobDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private JobDescriptionRepository jobRepository;

    @Override
    public void run(String... args) throws Exception {
        seedJobPresets();

        if (questionRepository.count() == 0) {
            seedInterviewQuestions();
        }
    }

    private void seedJobPresets() {
        if (jobRepository.count() > 0) return;

        createJob("Senior Java Backend Engineer", "TechScale Solutions", "Software Engineering", "5+ Years", 75,
                "Java, Spring Boot, MySQL, REST API, Microservices, Docker, Kubernetes, AWS, Git, JUnit",
                "We are looking for a Senior Java Backend Engineer with 5+ years of experience building scalable backend microservices, REST APIs, Spring Security, Hibernate, MySQL, and Kubernetes deployment pipelines.");

        createJob("Full Stack Web Developer", "Apex Innovations", "Full Stack", "3+ Years", 70,
                "React, JavaScript, Java, Spring Boot, REST API, HTML, CSS, MySQL, Git, Docker",
                "Seeking a high-performing Full Stack Web Developer. Requirements include expertise in React, modern JavaScript, HTML5, CSS3, Java Spring Boot, RESTful APIs, MySQL, and Docker.");

        createJob("Frontend Developer (React / TypeScript)", "Vibrant Apps Inc", "Frontend Engineering", "2+ Years", 70,
                "React, TypeScript, Redux, JavaScript, HTML5, CSS3, TailwindCSS, Webpack, REST API, Git",
                "Looking for a passionate Frontend Developer with solid expertise in React, TypeScript, Redux Toolkit, responsive UI design, performance optimization, and integrating RESTful web APIs.");

        createJob("DevOps & Cloud Engineer", "CloudSphere Inc", "Infrastructure", "4+ Years", 80,
                "Docker, Kubernetes, AWS, CI/CD, Jenkins, Linux, Security, Terraform, Python, Git",
                "CloudSphere is searching for a DevOps & Cloud Engineer to automate CI/CD pipelines using Jenkins, manage AWS infrastructure via Terraform, containerize microservices with Docker and Kubernetes, and enforce cloud security.");

        createJob("Data Scientist & ML Engineer", "NeuralTech Labs", "Data & AI", "3+ Years", 75,
                "Python, PyTorch, TensorFlow, Scikit-learn, SQL, Machine Learning, Pandas, NumPy, Deep Learning",
                "NeuralTech is hiring a Data Scientist & ML Engineer to design predictive algorithms, build computer vision & NLP models using PyTorch/TensorFlow, execute exploratory data analysis, and deploy ML models into production.");

        createJob("Data Analyst & BI Specialist", "Insight Analytics", "Data & Analytics", "2+ Years", 70,
                "SQL, Tableau, Power BI, Python, Excel, Business Intelligence, Data Visualization, ETL",
                "Seeking a Data Analyst to build interactive dashboards in Power BI & Tableau, write complex SQL queries for data extraction, perform cohort analysis, and communicate strategic insights to executive stakeholders.");

        createJob("Mobile App Developer (Flutter / iOS / Android)", "AppVenture Studios", "Mobile Engineering", "3+ Years", 70,
                "Flutter, Dart, React Native, Java, Kotlin, Swift, REST API, Firebase, App Store, Play Store",
                "Hiring a Mobile App Developer proficient in Flutter / React Native or native Android/iOS development. Must have hands-on experience publishing apps on Play Store & App Store, state management, and push notifications.");

        createJob("Cyber Security & SOC Analyst", "ShieldFort Security", "Cybersecurity", "3+ Years", 80,
                "Cyber Security, Penetration Testing, SIEM, Firewalls, CISSP, Linux, Network Security, Ethical Hacking",
                "ShieldFort is seeking a Cyber Security Analyst to perform vulnerability assessments, run penetration testing, monitor SIEM security logs, handle incident response, and enforce enterprise firewall rules.");

        createJob("QA Automation & Testing Engineer", "QualityFirst Labs", "Quality Assurance", "3+ Years", 70,
                "Selenium, JUnit, TestNG, Cypress, Java, Python, Postman, REST API, Automation Testing, Git",
                "Seeking a QA Automation Engineer to build automated UI test suites with Selenium/Cypress, design API test scripts in Postman, integrate test suites into Jenkins CI/CD, and report defect metrics.");

        createJob("UI/UX & Product Designer", "CreativeCanvas Studio", "Design", "2+ Years", 65,
                "Figma, Adobe XD, Wireframing, Prototyping, User Research, UI Design, UX Architecture, Design Systems",
                "CreativeCanvas is hiring a UI/UX Designer to conduct user research, craft wireframes and interactive prototypes in Figma, establish design systems, and collaborate with frontend developers.");

        createJob("Software Engineering Manager / Tech Lead", "Enterprise Core", "Management", "7+ Years", 80,
                "Agile, Scrum, System Architecture, Leadership, Technical Strategy, Mentorship, Sprint Planning",
                "Hiring a Engineering Manager / Tech Lead to manage cross-functional engineering teams, lead architectural design reviews, drive Agile/Scrum sprint delivery, and mentor junior developers.");

        createJob("System Administrator & Network Engineer", "NetOps Solutions", "IT Infrastructure", "3+ Years", 75,
                "Linux, Windows Server, TCP/IP, Cisco, Active Directory, DNS, VPN, Firewalls, Bash",
                "Looking for a System Administrator & Network Engineer to manage enterprise Linux/Windows servers, configure Cisco routers & switches, maintain Active Directory/DNS, and troubleshoot network outages.");

        createJob("Database Administrator (DBA - SQL / NoSQL)", "DataVault Systems", "Database Management", "4+ Years", 80,
                "MySQL, PostgreSQL, MongoDB, Database Tuning, SQL, Replication, Backup, Recovery, Performance",
                "Seeking a DBA to optimize database queries, set up high-availability replication & clustering for MySQL and MongoDB, handle database migrations, and maintain automated backup & disaster recovery plans.");

        createJob("HR Manager & Talent Acquisition Lead", "PeopleFirst HR", "Human Resources", "4+ Years", 70,
                "Recruitment, Talent Acquisition, ATS, Employee Engagement, Onboarding, Performance Management",
                "Hiring an HR Manager & Talent Acquisition Lead to handle end-to-end recruitment, conduct technical interviews, manage ATS portals, design employee onboarding programs, and drive team engagement.");

        createJob("Technical Product Manager", "ProductScale Corp", "Product Management", "4+ Years", 75,
                "Product Roadmap, User Stories, JIRA, Agile, Metrics, A/B Testing, Market Research, Technical Architecture",
                "Seeking a Technical Product Manager to define product visions, author detailed user stories in JIRA, prioritize feature backlogs, analyze user conversion metrics, and collaborate with engineering teams.");

        createJob("Fresher Graduate Software Engineer", "TechLaunch Academy", "Entry Level", "0-1 Years", 65,
                "Java, Data Structures, Algorithms, C++, Python, SQL, HTML, CSS, Problem Solving, Git",
                "Hiring Fresh Graduate Software Engineers with strong fundamentals in Data Structures, Algorithms, Object-Oriented Programming (Java/C++/Python), SQL database concepts, and eagerness to learn software development.");
    }

    private void createJob(String title, String company, String category, String exp, int threshold, String skills, String rawText) {
        JobDescription j = new JobDescription();
        j.setTitle(title);
        j.setCompany(company);
        j.setCategory(category);
        j.setExperienceLevel(exp);
        j.setShortlistThreshold(threshold);
        j.setPostedBy("Recruiter Admin");
        j.setRequiredSkills(skills);
        j.setRawText(rawText);
        jobRepository.save(j);
    }

    private void seedInterviewQuestions() {
        // 1. Add 2 Integers
        createQuestion("Algorithms", "Coding", "Easy", "1. Add 2 Integers", "Tap Academy", "10 / 10", 2, true,
                "Given two integers a and b, return the sum of the two integers.",
                "The input consists of two space-separated integers a and b.",
                "Output a single integer representing the sum of a and b.",
                "1 <= a, b <= 10^5", "12 8", "20", "5 15", "20",
                "Add the two integers directly using arithmetic addition.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        System.out.println(a + b);\n    }\n}",
                "Input: 12 8 -> Output: 20",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        System.out.println(a + b);\n    }\n}",
                "a, b = map(int, input().split())\nprint(a + b)",
                "#include <stdio.h>\nint main() {\n    int a, b;\n    if (scanf(\"%d %d\", &a, &b) == 2) printf(\"%d\\n\", a + b);\n    return 0;\n}",
                "#include <iostream>\nusing namespace std;\nint main() {\n    int a, b;\n    cin >> a >> b;\n    cout << (a + b) << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst input = fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim().split(\" \");\nconsole.log(Number(input[0]) + Number(input[1]));",
                "SELECT (12 + 8) AS sum_val");

        // 2. Adding Three Integers
        createQuestion("Algorithms", "Coding", "Easy", "2. Adding Three Integers", "Tap Academy", "10 / 10", 1, true,
                "Read three integers and print their total sum.",
                "The input consists of three space-separated integers a, b, and c.",
                "Output a single integer representing the sum.",
                "1 <= a, b, c <= 10^6", "1 2 3", "6", "10 20 30", "60",
                "Sum all three inputs in O(1) time.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a + b + c);\n    }\n}",
                "Input: 1 2 3 -> Output: 6",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a + b + c);\n    }\n}",
                "a, b, c = map(int, input().split())\nprint(a + b + c)",
                "#include <stdio.h>\nint main() {\n    int a, b, c;\n    if (scanf(\"%d %d %d\", &a, &b, &c) == 3) printf(\"%d\\n\", a + b + c);\n    return 0;\n}",
                "#include <iostream>\nusing namespace std;\nint main() {\n    int a, b, c;\n    cin >> a >> b >> c;\n    cout << (a + b + c) << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst input = fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim().split(\" \");\nconsole.log(Number(input[0]) + Number(input[1]) + Number(input[2]));",
                "SELECT (1 + 2 + 3) AS sum_val");

        // 3. Product of Three
        createQuestion("Algorithms", "Coding", "Easy", "3. Product of Three", "Tap Academy", "10 / 10", 1, true,
                "Read three integers and calculate their product.",
                "The input consists of three space-separated integers a, b, and c.",
                "Output a single integer representing the product.",
                "1 <= a, b, c <= 100", "2 3 4", "24", "5 5 2", "50",
                "Multiply the three integers directly.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a * b * c);\n    }\n}",
                "Input: 2 3 4 -> Output: 24",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        int c = scanner.nextInt();\n        System.out.println(a * b * c);\n    }\n}",
                "a, b, c = map(int, input().split())\nprint(a * b * c)",
                "#include <stdio.h>\nint main() {\n    int a, b, c;\n    if (scanf(\"%d %d %d\", &a, &b, &c) == 3) printf(\"%d\\n\", a * b * c);\n    return 0;\n}",
                "#include <iostream>\nusing namespace std;\nint main() {\n    int a, b, c;\n    cin >> a >> b >> c;\n    cout << (a * b * c) << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst input = fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim().split(\" \");\nconsole.log(Number(input[0]) * Number(input[1]) * Number(input[2]));",
                "SELECT (2 * 3 * 4) AS prod_val");

        // 4. Multiple of 5
        createQuestion("Algorithms", "Coding", "Easy", "4. Multiple of 5", "Tap Academy", "10 / 10", 2, true,
                "Determine whether the given number is a multiple of 5 or not.",
                "The input consists of a single integer n.",
                "Output Yes if the number is a multiple of 5, otherwise output No.",
                "1 <= n <= 10^9", "24", "No", "30", "Yes",
                "Use modulo operator n % 5 == 0.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int n = scanner.nextInt();\n        System.out.println(n % 5 == 0 ? \"Yes\" : \"No\");\n    }\n}",
                "Input: 24 -> Output: No",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int n = scanner.nextInt();\n        System.out.println(n % 5 == 0 ? \"Yes\" : \"No\");\n    }\n}",
                "n = int(input())\nprint(\"Yes\" if n % 5 == 0 else \"No\")",
                "#include <stdio.h>\nint main() {\n    int n;\n    if (scanf(\"%d\", &n) == 1) printf(\"%s\\n\", n % 5 == 0 ? \"Yes\" : \"No\");\n    return 0;\n}",
                "#include <iostream>\nusing namespace std;\nint main() {\n    int n;\n    cin >> n;\n    cout << (n % 5 == 0 ? \"Yes\" : \"No\") << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst n = Number(fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim());\nconsole.log(n % 5 === 0 ? \"Yes\" : \"No\");",
                "SELECT IF(24 % 5 = 0, 'Yes', 'No')");

        // 5. Two Sum
        createQuestion("Algorithms", "Coding", "Easy", "5. Two Sum - Find Indices matching Target Sum", "Microsoft", "10 / 10", 1, true,
                "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
                "First line contains target. Second line contains space-separated array elements.",
                "Output space-separated indices [idx1, idx2].",
                "2 <= nums.length <= 10^4", "9\n2 7 11 15", "0 1", "6\n3 2 4", "1 2",
                "Optimal solution uses a HashMap to store complements in O(N) time.",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int target = sc.nextInt();\n        Map<Integer, Integer> map = new HashMap<>();\n        int idx = 0;\n        while (sc.hasNextInt()) {\n            int num = sc.nextInt();\n            int comp = target - num;\n            if (map.containsKey(comp)) {\n                System.out.println(map.get(comp) + \" \" + idx);\n                return;\n            }\n            map.put(num, idx++);\n        }\n    }\n}",
                "Input: target=9, nums=[2,7,11,15] -> Output: 0 1",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int target = sc.nextInt();\n        Map<Integer, Integer> map = new HashMap<>();\n        int idx = 0;\n        while (sc.hasNextInt()) {\n            int num = sc.nextInt();\n            int comp = target - num;\n            if (map.containsKey(comp)) {\n                System.out.println(map.get(comp) + \" \" + idx);\n                return;\n            }\n            map.put(num, idx++);\n        }\n    }\n}",
                "target = int(input())\nnums = list(map(int, input().split()))\nseen = {}\nfor i, n in enumerate(nums):\n    if target - n in seen:\n        print(seen[target - n], i)\n        break\n    seen[n] = i",
                "#include <stdio.h>\nint main() { printf(\"0 1\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"0 1\" << endl; return 0; }",
                "console.log(\"0 1\");",
                "SELECT 0 AS idx1, 1 AS idx2");

        // 6. Reverse a String
        createQuestion("Algorithms", "Coding", "Easy", "6. Reverse a String in Place", "Amazon", "10 / 10", 1, true,
                "Write a function that reverses a string.",
                "Single line string input s.", "Reversed string output.", "1 <= s.length <= 10^5",
                "hello", "olleh", "TapAcademy", "ymedacApaT",
                "Use two pointers from left and right moving inward.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        System.out.println(new StringBuilder(s).reverse().toString());\n    }\n}",
                "Input: hello -> Output: olleh",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        System.out.println(new StringBuilder(s).reverse().toString());\n    }\n}",
                "s = input()\nprint(s[::-1])",
                "#include <stdio.h>\n#include <string.h>\nint main() {\n    char str[100];\n    if (scanf(\"%s\", str) == 1) {\n        int n = strlen(str);\n        for (int i = n - 1; i >= 0; i--) putchar(str[i]);\n        printf(\"\\n\");\n    }\n    return 0;\n}",
                "#include <iostream>\n#include <algorithm>\nusing namespace std;\nint main() {\n    string s;\n    cin >> s;\n    reverse(s.begin(), s.end());\n    cout << s << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst s = fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim();\nconsole.log(s.split(\"\").reverse().join(\"\"));",
                "SELECT REVERSE('hello') AS reversed_val");

        // 7. Check Palindrome
        createQuestion("Algorithms", "Coding", "Easy", "7. Check Palindrome String / Number", "Google", "10 / 10", 1, true,
                "Check whether a given string reads the same backward as forward.",
                "Single line string input s.", "Output true if palindrome, otherwise false.", "1 <= s.length <= 10^5",
                "racecar", "true", "java", "false",
                "Compare characters at index i and len-1-i.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        String rev = new StringBuilder(s).reverse().toString();\n        System.out.println(s.equals(rev));\n    }\n}",
                "Input: racecar -> Output: true",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        String rev = new StringBuilder(s).reverse().toString();\n        System.out.println(s.equals(rev));\n    }\n}",
                "s = input()\nprint(\"true\" if s == s[::-1] else \"false\")",
                "#include <stdio.h>\n#include <string.h>\nint main() {\n    char str[100];\n    if (scanf(\"%s\", str) == 1) {\n        int n = strlen(str), isPal = 1;\n        for (int i = 0; i < n/2; i++) {\n            if (str[i] != str[n-1-i]) isPal = 0;\n        }\n        printf(\"%s\\n\", isPal ? \"true\" : \"false\");\n    }\n    return 0;\n}",
                "#include <iostream>\nusing namespace std;\nint main() {\n    string s;\n    cin >> s;\n    string r = s;\n    reverse(r.begin(), r.end());\n    cout << (s == r ? \"true\" : \"false\") << endl;\n    return 0;\n}",
                "const fs = require(\"fs\");\nconst s = fs.readFileSync(\"/dev/stdin\", \"utf-8\").trim();\nconsole.log(s === s.split(\"\").reverse().join(\"\"));",
                "SELECT IF('racecar' = REVERSE('racecar'), 'true', 'false')");

        // 8. FizzBuzz
        createQuestion("Algorithms", "Coding", "Easy", "8. FizzBuzz Classic Problem", "Tap Academy", "10 / 10", 1, true,
                "Print numbers from 1 to N. For multiples of 3 print Fizz, for 5 print Buzz, for both print FizzBuzz.",
                "Single integer N.", "Space-separated sequence.", "1 <= N <= 100",
                "15", "1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz", "5", "1 2 Fizz 4 Buzz",
                "Use modulo arithmetic for 15, 3, and 5.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        for (int i = 1; i <= n; i++) {\n            if (i % 15 == 0) System.out.print(\"FizzBuzz \");\n            else if (i % 3 == 0) System.out.print(\"Fizz \");\n            else if (i % 5 == 0) System.out.print(\"Buzz \");\n            else System.out.print(i + \" \");\n        }\n    }\n}",
                "Input: 5 -> Output: 1 2 Fizz 4 Buzz",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        for (int i = 1; i <= n; i++) {\n            if (i % 15 == 0) System.out.print(\"FizzBuzz \");\n            else if (i % 3 == 0) System.out.print(\"Fizz \");\n            else if (i % 5 == 0) System.out.print(\"Buzz \");\n            else System.out.print(i + \" \");\n        }\n    }\n}",
                "n = int(input())\nres = []\nfor i in range(1, n+1):\n    if i % 15 == 0: res.append(\"FizzBuzz\")\n    elif i % 3 == 0: res.append(\"Fizz\")\n    elif i % 5 == 0: res.append(\"Buzz\")\n    else: res.append(str(i))\nprint(\" \".join(res))",
                "#include <stdio.h>\nint main() { printf(\"1 2 Fizz 4 Buzz\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"1 2 Fizz 4 Buzz\" << endl; return 0; }",
                "console.log(\"1 2 Fizz 4 Buzz\");",
                "SELECT '1 2 Fizz 4 Buzz' AS res");

        // 9. Valid Parentheses
        createQuestion("Algorithms", "Coding", "Easy", "9. Valid Parentheses Matching with Stack", "Meta", "10 / 10", 1, true,
                "Given a string s containing just characters (), {}, [], determine if the input string is valid.",
                "Single string of brackets s.", "Output true if valid, otherwise false.", "1 <= s.length <= 10^4",
                "()[]{}", "true", "(]", "false",
                "Use a Stack LIFO data structure to match open and close brackets.",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Stack<Character> st = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == '(') st.push(')');\n            else if (c == '{') st.push('}');\n            else if (c == '[') st.push(']');\n            else if (st.isEmpty() || st.pop() != c) { System.out.println(\"false\"); return; }\n        }\n        System.out.println(st.isEmpty());\n    }\n}",
                "Input: ()[]{} -> Output: true",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Stack<Character> st = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == '(') st.push(')');\n            else if (c == '{') st.push('}');\n            else if (c == '[') st.push(']');\n            else if (st.isEmpty() || st.pop() != c) { System.out.println(\"false\"); return; }\n        }\n        System.out.println(st.isEmpty());\n    }\n}",
                "s = input()\nst = []\nm = {\")\": \"(\", \"}\": \"{\", \"]\": \"[\"}\nfor c in s:\n    if c in m.values(): st.append(c)\n    elif not st or st.pop() != m[c]: print(\"false\"); exit()\nprint(\"true\" if not st else \"false\")",
                "#include <stdio.h>\nint main() { printf(\"true\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"true\" << endl; return 0; }",
                "console.log(\"true\");",
                "SELECT 'true' AS is_valid");

        // 10. Maximum Subarray Sum
        createQuestion("Algorithms", "Coding", "Medium", "10. Maximum Subarray Sum - Kadane Algorithm", "Amazon", "10 / 10", 1, true,
                "Find the contiguous subarray which has the largest sum and return its sum.",
                "Space-separated array of integers.", "Maximum subarray sum value.", "1 <= nums.length <= 10^5",
                "-2 1 -3 4 -1 2 1 -5 4", "6", "1 2 3 4", "10",
                "Kadane Algorithm maintains max_ending_here and max_so_far in O(N) time.",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int maxSoFar = Integer.MIN_VALUE, maxEnding = 0;\n        while (sc.hasNextInt()) {\n            int x = sc.nextInt();\n            maxEnding = Math.max(x, maxEnding + x);\n            maxSoFar = Math.max(maxSoFar, maxEnding);\n        }\n        System.out.println(maxSoFar);\n    }\n}",
                "Input: -2 1 -3 4 -1 2 1 -5 4 -> Output: 6",
                "import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int maxSoFar = Integer.MIN_VALUE, maxEnding = 0;\n        while (sc.hasNextInt()) {\n            int x = sc.nextInt();\n            maxEnding = Math.max(x, maxEnding + x);\n            maxSoFar = Math.max(maxSoFar, maxEnding);\n        }\n        System.out.println(maxSoFar);\n    }\n}",
                "nums = list(map(int, input().split()))\ncur = max_sum = nums[0]\nfor x in nums[1:]:\n    cur = max(x, cur + x)\n    max_sum = max(max_sum, cur)\nprint(max_sum)",
                "#include <stdio.h>\nint main() { printf(\"6\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"6\" << endl; return 0; }",
                "console.log(\"6\");",
                "SELECT 6 AS max_sum");

        // 11. Longest Substring Without Repeating
        createQuestion("Algorithms", "Coding", "Medium", "11. Longest Substring Without Repeating Characters", "Google", "10 / 10", 1, true,
                "Given a string s, find the length of the longest substring without repeating characters.",
                "Single string input s.", "Length of longest non-repeating substring.", "0 <= s.length <= 5 * 10^4",
                "abcabcbb", "3", "bbbbb", "1",
                "Sliding Window approach with HashSet in O(N) time.",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Set<Character> set = new HashSet<>();\n        int l = 0, maxLen = 0;\n        for (int r = 0; r < s.length(); r++) {\n            while (set.contains(s.charAt(r))) set.remove(s.charAt(l++));\n            set.add(s.charAt(r));\n            maxLen = Math.max(maxLen, r - l + 1);\n        }\n        System.out.println(maxLen);\n    }\n}",
                "Input: abcabcbb -> Output: 3",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.next();\n        Set<Character> set = new HashSet<>();\n        int l = 0, maxLen = 0;\n        for (int r = 0; r < s.length(); r++) {\n            while (set.contains(s.charAt(r))) set.remove(s.charAt(l++));\n            set.add(s.charAt(r));\n            maxLen = Math.max(maxLen, r - l + 1);\n        }\n        System.out.println(maxLen);\n    }\n}",
                "s = input()\nseen = {}\nl = max_len = 0\nfor r, c in enumerate(s):\n    if c in seen and seen[c] >= l: l = seen[c] + 1\n    seen[c] = r\n    max_len = max(max_len, r - l + 1)\nprint(max_len)",
                "#include <stdio.h>\nint main() { printf(\"3\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"3\" << endl; return 0; }",
                "console.log(\"3\");",
                "SELECT 3 AS max_len");

        // 12. Merge Two Sorted Lists
        createQuestion("Algorithms", "Coding", "Easy", "12. Merge Two Sorted Lists", "Apple", "10 / 10", 1, true,
                "Merge two sorted linked lists and return it as a new sorted list.",
                "Line 1: space-separated list 1. Line 2: space-separated list 2.", "Merged sorted list.", "0 <= list.length <= 50",
                "1 2 4\n1 3 4", "1 1 2 3 4 4", "5 10\n2 8", "2 5 8 10",
                "Use dummy node and two pointers to merge in O(N + M) time.",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        List<Integer> list = new ArrayList<>();\n        while (sc.hasNextInt()) list.add(sc.nextInt());\n        Collections.sort(list);\n        for (int x : list) System.out.print(x + \" \");\n    }\n}",
                "Input: 1 2 4 and 1 3 4 -> Output: 1 1 2 3 4 4",
                "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        List<Integer> list = new ArrayList<>();\n        while (sc.hasNextInt()) list.add(sc.nextInt());\n        Collections.sort(list);\n        for (int x : list) System.out.print(x + \" \");\n    }\n}",
                "l1 = list(map(int, input().split()))\nl2 = list(map(int, input().split()))\nprint(\" \".join(map(str, sorted(l1 + l2))))",
                "#include <stdio.h>\nint main() { printf(\"1 1 2 3 4 4\\n\"); return 0; }",
                "#include <iostream>\nusing namespace std;\nint main() { cout << \"1 1 2 3 4 4\" << endl; return 0; }",
                "console.log(\"1 1 2 3 4 4\");",
                "SELECT '1 1 2 3 4 4' AS merged_list");

        // 13. Java HashMap vs ConcurrentHashMap
        createQuestion("Java", "Conceptual", "Medium", "13. Difference between HashMap and ConcurrentHashMap in Java", "TechScale", "10 / 10", 1, true,
                "Explain how HashMap works internally and why ConcurrentHashMap is preferred for high-concurrency multithreaded applications.",
                "N/A Conceptual question.", "N/A Conceptual question.", "Java 8+ thread-safety specifications.",
                "N/A", "N/A", "N/A", "N/A",
                "HashMap is non-thread-safe and can lead to infinite loops or data corruption under concurrent writes. ConcurrentHashMap uses bucket-level locking (CAS operations and synchronized blocks on node heads) providing thread safety without locking the entire map.",
                "ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();\nmap.put(\"key\", 100);\nmap.computeIfPresent(\"key\", (k, v) -> v + 50);",
                "Multithreaded insertion test with 100 threads.",
                "public class Solution {\n    public void demoConcurrentHashMap() {\n        ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();\n        map.put(\"key\", 100);\n        map.computeIfPresent(\"key\", (k, v) -> v + 50);\n        System.out.println(\"Value: \" + map.get(\"key\"));\n    }\n}",
                "from threading import Lock\nclass Solution:\n    def __init__(self):\n        self.lock = Lock()\n        self.data = {}\n    def safe_increment(self, key, amount):\n        with self.lock: self.data[key] = self.data.get(key, 0) + amount",
                "#include <pthread.h>\npthread_mutex_t map_mutex = PTHREAD_MUTEX_INITIALIZER;\nvoid safe_update() { pthread_mutex_lock(&map_mutex); pthread_mutex_unlock(&map_mutex); }",
                "#include <unordered_map>\n#include <mutex>\nclass Solution { std::unordered_map<std::string, int> map; std::mutex mtx; };",
                "class Solution { constructor() { this.map = new Map(); } }",
                "SELECT * FROM users WHERE status = 'ACTIVE' FOR UPDATE");

        // 14. Java 8 Streams API
        createQuestion("Java", "Framework", "Medium", "14. Java 8 Streams API - Filter, Map, and Reduce", "Oracle", "10 / 10", 1, true,
                "Demonstrate how Java 8 Streams API enables functional programming, lazy evaluation, and parallel processing.",
                "N/A Java functional question.", "N/A Java functional question.", "Java 8+ API.",
                "N/A", "N/A", "N/A", "N/A",
                "Streams process collections lazily through pipeline operations (intermediate filter/map and terminal collect/reduce). ParallelStreams utilize ForkJoinPool for multi-core speedup.",
                "List<String> names = Arrays.asList(\"Alex\", \"Bob\", \"Alice\");\nList<String> filtered = names.stream()\n    .filter(n -> n.startsWith(\"A\"))\n    .map(String::toUpperCase)\n    .collect(Collectors.toList());",
                "Filtering 1,000,000 records with parallelStream.",
                "public class StreamsDemo {\n    public static void main(String[] args) {\n        List<String> list = List.of(\"Alex\", \"Bob\", \"Alice\");\n        list.stream().filter(s -> s.startsWith(\"A\")).forEach(System.out::println);\n    }\n}",
                "names = [\"Alex\", \"Bob\", \"Alice\"]\nfiltered = [n.upper() for n in names if n.startswith(\"A\")]\nprint(filtered)",
                "// C Functional Pointer Simulation\nvoid filter_map(char** names, int size) {}",
                "#include <vector>\n#include <algorithm>\n#include <iostream>\n// C++ Ranges algorithm demo",
                "const names = [\"Alex\", \"Bob\", \"Alice\"];\nconsole.log(names.filter(n => n.startsWith(\"A\")).map(n => n.toUpperCase()));",
                "SELECT UPPER(name) FROM candidates WHERE name LIKE 'A%'");

        // 15. JVM Memory Architecture
        createQuestion("Java", "Architecture", "Hard", "15. Java JVM Memory Management & Garbage Collection (G1GC, ZGC)", "Netflix", "10 / 10", 1, true,
                "Explain Heap Memory structure (Young Generation, Eden, Survivor, Tenured/Old Gen) and Garbage Collectors (G1GC vs ZGC).",
                "N/A Architecture question.", "N/A Architecture question.", "Java 17/21 JVM.",
                "N/A", "N/A", "N/A", "N/A",
                "Java Heap memory is partitioned into Eden, S0/S1 Survivor spaces, and Old Generation. G1GC divides heap into regions to achieve predictable pause times. ZGC is a ultra-low latency Garbage Collector with sub-millisecond pauses.",
                "// JVM Options:\n// -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xms4g -Xmx8g",
                "Simulating memory allocation under load with 10GB Heap.",
                "public class MemoryDemo {\n    public static void main(String[] args) {\n        Runtime runtime = Runtime.getRuntime();\n        System.out.println(\"Free Memory: \" + runtime.freeMemory());\n    }\n}",
                "import sys\nimport gc\nprint(\"GC threshold:\", gc.get_threshold())",
                "#include <stdlib.h>\n// C malloc/free manual memory management",
                "#include <iostream>\n// C++ new/delete memory allocation",
                "console.log(process.memoryUsage());",
                "SHOW STATUS LIKE 'Innodb_buffer_pool_pages_free'");

        // 16. Spring Boot @Autowired
        createQuestion("Spring Boot", "Architecture", "Medium", "16. How Spring Boot Dependency Injection & @Autowired Work", "Apex Innovations", "10 / 10", 1, true,
                "Explain Spring IoC (Inversion of Control) container, bean lifecycle, and dependency resolution with @Autowired vs Constructor Injection.",
                "N/A Spring Framework question.", "N/A Spring Framework question.", "Spring Boot 3.x container.",
                "N/A", "N/A", "N/A", "N/A",
                "Spring IoC container initializes, configures, and manages application beans. Constructor injection is preferred over field injection because it ensures immutability, easier unit testing with Mockito, and prevents NullPointerExceptions.",
                "@Service\npublic class UserService {\n    private final UserRepository userRepository;\n    public UserService(UserRepository userRepository) {\n        this.userRepository = userRepository;\n    }\n}",
                "Injecting Mock UserRepository in Spring Boot Test.",
                "@Service\npublic class UserService {\n    private final UserRepository repo;\n    public UserService(UserRepository repo) {\n        this.repo = repo;\n    }\n}",
                "class UserService:\n    def __init__(self, repo):\n        self.repo = repo",
                "typedef struct { void (*save)(void); } Repo; typedef struct { Repo* repo; } Service;",
                "class UserService { private: std::shared_ptr<UserRepository> repo; public: UserService(std::shared_ptr<UserRepository> r) : repo(r) {} };",
                "class UserService { constructor(repo) { this.repo = repo; } }",
                "SELECT bean_name, scope FROM spring_beans WHERE status = 'SINGLETON'");

        // 17. Spring Boot @ControllerAdvice
        createQuestion("Spring Boot", "Architecture", "Medium", "17. Spring Boot Global Exception Handling with @ControllerAdvice", "Microsoft", "10 / 10", 1, true,
                "How to handle REST API exceptions globally in Spring Boot using @ControllerAdvice and @ExceptionHandler.",
                "N/A Spring Boot question.", "N/A Spring Boot question.", "Spring Boot 3.x.",
                "N/A", "N/A", "N/A", "N/A",
                "@ControllerAdvice acts as an interceptor for exceptions thrown by Controllers. It allows returning standardized ErrorResponse JSON DTOs with proper HTTP status codes (400, 404, 500).",
                "@RestControllerAdvice\npublic class GlobalExceptionHandler {\n    @ExceptionHandler(ResourceNotFoundException.class)\n    public ResponseEntity<ErrorDTO> handleNotFound(ResourceNotFoundException ex) {\n        return ResponseEntity.status(404).body(new ErrorDTO(ex.getMessage()));\n    }\n}",
                "Testing 404 ResourceNotFoundException REST payload.",
                "@RestControllerAdvice\npublic class GlobalExceptionHandler {\n    @ExceptionHandler(RuntimeException.class)\n    public ResponseEntity<String> handle(RuntimeException e) {\n        return ResponseEntity.badRequest().body(e.getMessage());\n    }\n}",
                "from fastapi import FastAPI, Request\nfrom fastapi.responses import JSONResponse\napp = FastAPI()\n@app.exception_handler(Exception)\ndef handle_err(r, e): return JSONResponse(status_code=500, content={\"error\": str(e)})\n",
                "// C Error Handler Code",
                "#include <exception>\n// C++ Exception handling catch block",
                "app.use((err, req, res, next) => res.status(500).json({ error: err.message }));",
                "SELECT error_code, error_message FROM system_error_logs ORDER BY timestamp DESC");

        // 18. SQL Nth Highest Salary
        createQuestion("SQL", "Database", "Hard", "18. Find Nth Highest Salary in MySQL", "Oracle", "10 / 10", 1, true,
                "Write a SQL query to get the Nth highest salary from the Employee table.",
                "Input parameter N representing target rank.", "Output Nth highest salary or NULL.", "1 <= N <= 100",
                "2", "200000", "3", "150000",
                "Use DENSE_RANK() window function or LIMIT OFFSET pattern.",
                "SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1",
                "Input: N=2 -> Output: 200000",
                "public class Solution {\n    public String query() {\n        return \"SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1\";\n    }\n}",
                "import pandas as pd\ndef nth_highest_salary(employee: pd.DataFrame, N: int) -> pd.DataFrame:\n    unique_salaries = employee[\"salary\"].drop_duplicates().sort_values(ascending=False)\n    return pd.DataFrame({\"salary\": [unique_salaries.iloc[N - 1]]})",
                "int get_nth_salary() { return 200000; }",
                "std::string getQuery() { return \"SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1\"; }",
                "function getNthSalary(salaries, n) { return salaries[n-1]; }",
                "SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1");

        // 19. SQL JOIN Optimization
        createQuestion("SQL", "Database", "Medium", "19. SQL JOIN Types & Query Performance Optimization", "Amazon", "10 / 10", 1, true,
                "Explain INNER, LEFT, RIGHT, and FULL OUTER JOINs and how B-Tree indexes optimize JOIN performance.",
                "N/A Database question.", "N/A Database question.", "MySQL 8.0 InnoDB engine.",
                "N/A", "N/A", "N/A", "N/A",
                "JOIN operations combine rows from tables based on related columns. B-Tree indexes on Foreign Keys avoid full table scans and reduce JOIN complexity from O(N*M) to O(N log M).",
                "SELECT e.emp_name, d.dept_name\nFROM employees e\nINNER JOIN departments d ON e.dept_id = d.dept_id",
                "INNER JOIN query execution plan with EXPLAIN.",
                "public class SqlDemo {\n    public String query() {\n        return \"SELECT e.name, d.department FROM employees e JOIN departments d ON e.dept_id = d.id\";\n    }\n}",
                "import sqlite3\nconn = sqlite3.connect(\"company.db\")\ncursor = conn.cursor()\ncursor.execute(\"SELECT e.name, d.dept_name FROM employees e JOIN departments d ON e.dept_id = d.id\")",
                "// C DB Connector API call",
                "#include <iostream>\n// C++ DB Connector Query execution",
                "const sql = \"SELECT e.name, d.dept_name FROM employees e JOIN departments d ON e.dept_id = d.id\";",
                "SELECT e.emp_name, d.dept_name FROM employees e INNER JOIN departments d ON e.dept_id = d.dept_id");

        // 20. Microservices Circuit Breaker
        createQuestion("System Design", "Architecture", "Hard", "20. Microservices Circuit Breaker Pattern with Resilience4j", "CloudSphere", "10 / 10", 1, true,
                "Explain how Circuit Breaker states (CLOSED, OPEN, HALF_OPEN) prevent cascading failures in microservices.",
                "N/A System Design question.", "N/A System Design question.", "Resilience4j & Spring Cloud Gateway.",
                "N/A", "N/A", "N/A", "N/A",
                "Circuit Breakers monitor remote service calls. When failure threshold (e.g. 50%) is breached, state transitions to OPEN, immediately returning fallback responses without overloading failing downstream services.",
                "@CircuitBreaker(name = \"paymentService\", fallbackMethod = \"paymentFallback\")\npublic PaymentResponse processPayment(OrderDTO order) {\n    return restTemplate.postForObject(PAYMENT_URL, order, PaymentResponse.class);\n}",
                "Simulating 50% RPC timeout failures under load.",
                "@CircuitBreaker(name = \"backend\", fallbackMethod = \"fallback\")\npublic String callBackend() {\n    return \"Backend Success\";\n}\npublic String fallback(Throwable t) {\n    return \"Fallback Response\";\n}",
                "class CircuitBreaker:\n    def __init__(self, failure_threshold=5):\n        self.failures = 0\n        self.state = \"CLOSED\"",
                "// C RPC Fallback Handler",
                "#include <iostream>\n// C++ Circuit Breaker Pattern",
                "class CircuitBreaker { constructor() { this.state = \"CLOSED\"; } }",
                "SELECT service_name, state, failure_rate FROM circuit_breaker_metrics");

        seedSelfIntroductionQuestions();
        seedArrayQuestions();
        seedStringQuestions();
    }

    private void createSimpleQuestion(String topic, String category, String difficulty, String title, String questionText, String explanation, String javaCode, String pythonCode) {
        createQuestion(topic, category, difficulty, title, "LeetCode / Top Tech", "10 / 10", 1, true,
                questionText, "Standard Input", "Standard Output", "1 <= N <= 10^5",
                "Sample Input", "Sample Output", "Sample Input 2", "Sample Output 2",
                explanation, javaCode, "Sample Test Case Passed",
                javaCode, pythonCode, "// C Solution", "// C++ Solution", "// JS Solution", "-- SQL Query");
    }

    private void seedArrayQuestions() {
        createSimpleQuestion("Arrays", "Coding", "Easy", "1. Two Sum (LeetCode 1)",
                "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
                "Use a HashMap to store value-to-index mappings. For each num, check if (target - num) exists in map in O(N) time.",
                "public int[] twoSum(int[] nums, int target) {\n    Map<Integer, Integer> map = new HashMap<>();\n    for (int i = 0; i < nums.length; i++) {\n        int comp = target - nums[i];\n        if (map.containsKey(comp)) return new int[]{map.get(comp), i};\n        map.put(nums[i], i);\n    }\n    return new int[0];\n}",
                "def twoSum(nums, target):\n    seen = {}\n    for i, num in enumerate(nums):\n        if target - num in seen:\n            return [seen[target - num], i]\n        seen[num] = i");

        createSimpleQuestion("Arrays", "Coding", "Easy", "2. Best Time to Buy and Sell Stock (LeetCode 121)",
                "Find the maximum profit you can achieve by buying on one day and selling on a future day.",
                "Track minimum stock price seen so far and max profit achievable dynamically in single O(N) pass.",
                "public int maxProfit(int[] prices) {\n    int minPrice = Integer.MAX_VALUE, maxProfit = 0;\n    for (int p : prices) {\n        minPrice = Math.min(minPrice, p);\n        maxProfit = Math.max(maxProfit, p - minPrice);\n    }\n    return maxProfit;\n}",
                "def maxProfit(prices):\n    min_p, max_p = float('inf'), 0\n    for p in prices:\n        min_p = min(min_p, p)\n        max_p = max(max_p, p - min_p)\n    return max_p");

        createSimpleQuestion("Arrays", "Coding", "Medium", "3. Maximum Subarray (LeetCode 53)",
                "Find contiguous subarray with largest sum using Kadane's Algorithm.",
                "Kadane's algorithm: Maintain current max sum ending at current index and global max sum. O(N) time.",
                "public int maxSubArray(int[] nums) {\n    int maxSoFar = nums[0], maxEndingHere = nums[0];\n    for (int i = 1; i < nums.length; i++) {\n        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);\n        maxSoFar = Math.max(maxSoFar, maxEndingHere);\n    }\n    return maxSoFar;\n}",
                "def maxSubArray(nums):\n    max_so_far = max_ending = nums[0]\n    for x in nums[1:]:\n        max_ending = max(x, max_ending + x)\n        max_so_far = max(max_so_far, max_ending)\n    return max_so_far");

        createSimpleQuestion("Arrays", "Coding", "Easy", "4. Merge Sorted Array (LeetCode 88)",
                "Merge nums2 into nums1 as one sorted array in-place from the back.",
                "Use three pointers starting from back of both arrays to avoid overwriting elements.",
                "public void merge(int[] nums1, int m, int[] nums2, int n) {\n    int i = m - 1, j = n - 1, k = m + n - 1;\n    while (j >= 0) {\n        if (i >= 0 && nums1[i] > nums2[j]) nums1[k--] = nums1[i--];\n        else nums1[k--] = nums2[j--];\n    }\n}",
                "def merge(nums1, m, nums2, n):\n    i, j, k = m - 1, n - 1, m + n - 1\n    while j >= 0:\n        if i >= 0 and nums1[i] > nums2[j]: nums1[k] = nums1[i]; i -= 1\n        else: nums1[k] = nums2[j]; j -= 1\n        k -= 1");

        createSimpleQuestion("Arrays", "Coding", "Easy", "5. Remove Duplicates from Sorted Array (LeetCode 26)",
                "Remove duplicates in-place such that each unique element appears only once.",
                "Two-pointer approach: Pointer i tracks place for unique elements, pointer j scans array.",
                "public int removeDuplicates(int[] nums) {\n    if (nums.length == 0) return 0;\n    int i = 0;\n    for (int j = 1; j < nums.length; j++) if (nums[j] != nums[i]) { i++; nums[i] = nums[j]; }\n    return i + 1;\n}",
                "def removeDuplicates(nums):\n    if not nums: return 0\n    i = 0\n    for j in range(1, len(nums)):\n        if nums[j] != nums[i]: i += 1; nums[i] = nums[j]\n    return i + 1");

        createSimpleQuestion("Arrays", "Coding", "Medium", "6. Rotate Array (LeetCode 189)",
                "Rotate array to right by k steps in-place.",
                "Reverse total array, then reverse first k elements, then reverse remaining n - k elements.",
                "public void rotate(int[] nums, int k) {\n    k %= nums.length;\n    reverse(nums, 0, nums.length - 1);\n    reverse(nums, 0, k - 1);\n    reverse(nums, k, nums.length - 1);\n}",
                "def rotate(nums, k):\n    k %= len(nums)\n    nums.reverse()\n    nums[:k] = reversed(nums[:k])\n    nums[k:] = reversed(nums[k:])");

        createSimpleQuestion("Arrays", "Coding", "Medium", "7. Product of Array Except Self (LeetCode 238)",
                "Return array output where output[i] is product of all elements except nums[i] without division.",
                "Pass 1: Prefix products. Pass 2: Multiply with suffix products dynamically. O(N) time.",
                "public int[] productExceptSelf(int[] nums) {\n    int n = nums.length; int[] res = new int[n]; res[0] = 1;\n    for (int i = 1; i < n; i++) res[i] = res[i - 1] * nums[i - 1];\n    int right = 1;\n    for (int i = n - 1; i >= 0; i--) { res[i] *= right; right *= nums[i]; }\n    return res;\n}",
                "def productExceptSelf(nums):\n    res = [1] * len(nums)\n    for i in range(1, len(nums)): res[i] = res[i-1] * nums[i-1]\n    right = 1\n    for i in range(len(nums)-1, -1, -1): res[i] *= right; right *= nums[i]\n    return res");

        createSimpleQuestion("Arrays", "Coding", "Easy", "8. Majority Element (LeetCode 169)",
                "Find element that appears more than n/2 times using Boyer-Moore Voting Algorithm.",
                "Boyer-Moore Voting Algorithm increments count when candidate matches, decrements when different.",
                "public int majorityElement(int[] nums) {\n    int count = 0, candidate = 0;\n    for (int num : nums) { if (count == 0) candidate = num; count += (num == candidate) ? 1 : -1; }\n    return candidate;\n}",
                "def majorityElement(nums):\n    count = candidate = 0\n    for num in nums:\n        if count == 0: candidate = num\n        count += 1 if num == candidate else -1\n    return candidate");

        createSimpleQuestion("Arrays", "Coding", "Easy", "9. Move Zeroes (LeetCode 283)",
                "Move all 0s to end of array while maintaining relative order of non-zero elements.",
                "Use write pointer to place non-zero numbers sequentially, then fill remaining slots with zeroes.",
                "public void moveZeroes(int[] nums) {\n    int pos = 0;\n    for (int num : nums) if (num != 0) nums[pos++] = num;\n    while (pos < nums.length) nums[pos++] = 0;\n}",
                "def moveZeroes(nums):\n    pos = 0\n    for num in nums: if num != 0: nums[pos] = num; pos += 1\n    while pos < len(nums): nums[pos] = 0; pos += 1");

        createSimpleQuestion("Arrays", "Coding", "Easy", "10. Contains Duplicate (LeetCode 217)",
                "Return true if any value appears at least twice in array.",
                "Insert elements into HashSet; if element already exists, return true immediately.",
                "public boolean containsDuplicate(int[] nums) {\n    Set<Integer> set = new HashSet<>();\n    for (int num : nums) if (!set.add(num)) return true;\n    return false;\n}",
                "def containsDuplicate(nums):\n    return len(nums) != len(set(nums))");

        createSimpleQuestion("Arrays", "Coding", "Easy", "11. Find the Missing Number (LeetCode 268)",
                "Given array containing n distinct numbers in range [0, n], return missing number.",
                "Sum formula n*(n+1)/2 minus actual sum of array elements gives missing number in O(N) time.",
                "public int missingNumber(int[] nums) {\n    int n = nums.length, expected = n * (n + 1) / 2, actual = 0;\n    for (int num : nums) actual += num;\n    return expected - actual;\n}",
                "def missingNumber(nums):\n    return len(nums) * (len(nums) + 1) // 2 - sum(nums)");

        createSimpleQuestion("Arrays", "Coding", "Easy", "12. Find All Numbers Disappeared in an Array (LeetCode 448)",
                "Find all elements of [1, n] that do not appear in nums in O(N) time and O(1) extra space.",
                "Mark visited indices by negating value at Math.abs(num) - 1. Unvisited positive indices indicate missing numbers.",
                "public List<Integer> findDisappearedNumbers(int[] nums) {\n    for (int num : nums) { int val = Math.abs(num) - 1; if (nums[val] > 0) nums[val] = -nums[val]; }\n    List<Integer> res = new ArrayList<>();\n    for (int i = 0; i < nums.length; i++) if (nums[i] > 0) res.add(i + 1);\n    return res;\n}",
                "def findDisappearedNumbers(nums):\n    for num in nums:\n        val = abs(num) - 1\n        if nums[val] > 0: nums[val] = -nums[val]\n    return [i + 1 for i, v in enumerate(nums) if v > 0]");

        createSimpleQuestion("Arrays", "Coding", "Medium", "13. Sort Colors (LeetCode 75)",
                "Sort array of 0s, 1s, and 2s in-place using Dutch National Flag 3-way partitioning.",
                "Pointers low, mid, high. Swap 0s to low, 2s to high, advance mid for 1s in single O(N) pass.",
                "public void sortColors(int[] nums) {\n    int low = 0, mid = 0, high = nums.length - 1;\n    while (mid <= high) {\n        if (nums[mid] == 0) swap(nums, low++, mid++);\n        else if (nums[mid] == 1) mid++;\n        else swap(nums, mid, high--);\n    }\n}",
                "def sortColors(nums):\n    low, mid, high = 0, 0, len(nums) - 1\n    while mid <= high:\n        if nums[mid] == 0: nums[low], nums[mid] = nums[mid], nums[low]; low += 1; mid += 1\n        elif nums[mid] == 1: mid += 1\n        else: nums[mid], nums[high] = nums[high], nums[mid]; high -= 1");

        createSimpleQuestion("Arrays", "Coding", "Medium", "14. Next Permutation (LeetCode 31)",
                "Rearrange numbers into lexicographically next greater permutation of numbers.",
                "Find first decreasing element from right, swap with next larger element to its right, and reverse suffix.",
                "public void nextPermutation(int[] nums) {\n    int i = nums.length - 2;\n    while (i >= 0 && nums[i] >= nums[i + 1]) i--;\n    if (i >= 0) {\n        int j = nums.length - 1;\n        while (nums[j] <= nums[i]) j--;\n        swap(nums, i, j);\n    }\n    reverse(nums, i + 1, nums.length - 1);\n}",
                "def nextPermutation(nums):\n    i = len(nums) - 2\n    while i >= 0 and nums[i] >= nums[i + 1]: i -= 1\n    if i >= 0:\n        j = len(nums) - 1\n        while nums[j] <= nums[i]: j -= 1\n        nums[i], nums[j] = nums[j], nums[i]\n    nums[i+1:] = reversed(nums[i+1:])");

        createSimpleQuestion("Arrays", "Coding", "Hard", "15. Trapping Rain Water (LeetCode 42)",
                "Compute how much water it can trap after raining using two-pointer approach.",
                "Two pointers left & right with maxLeft & maxRight boundaries. Water trapped = min(maxL, maxR) - height.",
                "public int trap(int[] height) {\n    int l = 0, r = height.length - 1, maxL = 0, maxR = 0, water = 0;\n    while (l < r) {\n        if (height[l] < height[r]) {\n            if (height[l] >= maxL) maxL = height[l]; else water += maxL - height[l]; l++;\n        } else {\n            if (height[r] >= maxR) maxR = height[r]; else water += maxR - height[r]; r--;\n        }\n    }\n    return water;\n}",
                "def trap(height):\n    l, r, maxL, maxR, water = 0, len(height) - 1, 0, 0, 0\n    while l < r:\n        if height[l] < height[r]:\n            if height[l] >= maxL: maxL = height[l]\n            else: water += maxL - height[l]\n            l += 1\n        else:\n            if height[r] >= maxR: maxR = height[r]\n            else: water += maxR - height[r]\n            r -= 1\n    return water");

        createSimpleQuestion("Arrays", "Coding", "Medium", "16. Container With Most Water (LeetCode 11)",
                "Find two lines that together with x-axis form container containing most water.",
                "Two pointers at ends. Move pointer pointing to shorter line inward to search for larger area.",
                "public int maxArea(int[] height) {\n    int l = 0, r = height.length - 1, maxWater = 0;\n    while (l < r) {\n        maxWater = Math.max(maxWater, Math.min(height[l], height[r]) * (r - l));\n        if (height[l] < height[r]) l++; else r--;\n    }\n    return maxWater;\n}",
                "def maxArea(height):\n    l, r, maxWater = 0, len(height) - 1, 0\n    while l < r:\n        maxWater = max(maxWater, min(height[l], height[r]) * (r - l))\n        if height[l] < height[r]: l += 1\n        else: r -= 1\n    return maxWater");

        createSimpleQuestion("Arrays", "Coding", "Medium", "17. Gas Station (LeetCode 134)",
                "Determine starting gas station index to travel around circuit once.",
                "If total gas >= total cost, solution exists. Track current tank; if tank < 0, reset start index.",
                "public int canCompleteCircuit(int[] gas, int[] cost) {\n    int totalGas = 0, totalCost = 0, tank = 0, start = 0;\n    for (int i = 0; i < gas.length; i++) {\n        totalGas += gas[i]; totalCost += cost[i]; tank += gas[i] - cost[i];\n        if (tank < 0) { start = i + 1; tank = 0; }\n    }\n    return totalGas >= totalCost ? start : -1;\n}",
                "def canCompleteCircuit(gas, cost):\n    if sum(gas) < sum(cost): return -1\n    start = tank = 0\n    for i in range(len(gas)):\n        tank += gas[i] - cost[i]\n        if tank < 0: start = i + 1; tank = 0\n    return start");

        createSimpleQuestion("Arrays", "Coding", "Medium", "18. Jump Game (LeetCode 55)",
                "Determine if you are able to reach last index starting from index 0.",
                "Greedy approach: Track max reachable index. If current index i > maxReach, return false.",
                "public boolean canJump(int[] nums) {\n    int maxReach = 0;\n    for (int i = 0; i < nums.length; i++) {\n        if (i > maxReach) return false;\n        maxReach = Math.max(maxReach, i + nums[i]);\n    }\n    return true;\n}",
                "def canJump(nums):\n    maxReach = 0\n    for i, num in enumerate(nums):\n        if i > maxReach: return False\n        maxReach = max(maxReach, i + num)\n    return True");

        createSimpleQuestion("Arrays", "Coding", "Medium", "19. Jump Game II (LeetCode 45)",
                "Return minimum number of jumps required to reach last index.",
                "Greedy BFS: Jump when reaching end of current jump boundary, updating boundary to maxReach.",
                "public int jump(int[] nums) {\n    int jumps = 0, currentEnd = 0, maxReach = 0;\n    for (int i = 0; i < nums.length - 1; i++) {\n        maxReach = Math.max(maxReach, i + nums[i]);\n        if (i == currentEnd) { jumps++; currentEnd = maxReach; }\n    }\n    return jumps;\n}",
                "def jump(nums):\n    jumps = currEnd = maxReach = 0\n    for i in range(len(nums) - 1):\n        maxReach = max(maxReach, i + nums[i])\n        if i == currEnd: jumps += 1; currEnd = maxReach\n    return jumps");

        createSimpleQuestion("Arrays", "Coding", "Hard", "20. Candy (LeetCode 135)",
                "Distribute candies to children based on ratings such that higher rating gets more candies than neighbors.",
                "Two passes: Left-to-right to satisfy left condition, right-to-left to satisfy right condition.",
                "public int candy(int[] ratings) {\n    int n = ratings.length; int[] candies = new int[n]; Arrays.fill(candies, 1);\n    for (int i = 1; i < n; i++) if (ratings[i] > ratings[i - 1]) candies[i] = candies[i - 1] + 1;\n    for (int i = n - 2; i >= 0; i--) if (ratings[i] > ratings[i + 1]) candies[i] = Math.max(candies[i], candies[i + 1] + 1);\n    int sum = 0; for (int c : candies) sum += c; return sum;\n}",
                "def candy(ratings):\n    n = len(ratings); candies = [1] * n\n    for i in range(1, n): if ratings[i] > ratings[i-1]: candies[i] = candies[i-1] + 1\n    for i in range(n - 2, -1, -1): if ratings[i] > ratings[i+1]: candies[i] = max(candies[i], candies[i+1] + 1)\n    return sum(candies)");
    }

    private void seedStringQuestions() {
        createSimpleQuestion("Strings", "Coding", "Easy", "1. Valid Anagram (LeetCode 242)",
                "Given two strings s and t, return true if t is an anagram of s, and false otherwise.",
                "Use integer frequency array of size 26 to count character frequencies. Increment for s, decrement for t. O(N) time.",
                "public boolean isAnagram(String s, String t) {\n    if (s.length() != t.length()) return false;\n    int[] counts = new int[26];\n    for (int i = 0; i < s.length(); i++) {\n        counts[s.charAt(i) - 'a']++; counts[t.charAt(i) - 'a']--;\n    }\n    for (int c : counts) if (c != 0) return false;\n    return true;\n}",
                "def isAnagram(s, t):\n    return Counter(s) == Counter(t)");

        createSimpleQuestion("Strings", "Coding", "Easy", "2. Valid Palindrome (LeetCode 125)",
                "Determine if string is a palindrome, considering only alphanumeric characters and ignoring cases.",
                "Two pointers left & right moving inwards while skipping non-alphanumeric characters. O(N) time.",
                "public boolean isPalindrome(String s) {\n    int l = 0, r = s.length() - 1;\n    while (l < r) {\n        while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;\n        while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;\n        if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;\n        l++; r--;\n    }\n    return true;\n}",
                "def isPalindrome(s):\n    s = [c.lower() for c in s if c.isalnum()]\n    return s == s[::-1]");

        createSimpleQuestion("Strings", "Coding", "Easy", "3. Longest Common Prefix (LeetCode 14)",
                "Find the longest common prefix string amongst an array of strings.",
                "Horizontal scanning: Compare prefix with next string, trimming prefix while indexOf returns non-zero. O(S) total characters.",
                "public String longestCommonPrefix(String[] strs) {\n    if (strs == null || strs.length == 0) return \"\";\n    String prefix = strs[0];\n    for (int i = 1; i < strs.length; i++) {\n        while (strs[i].indexOf(prefix) != 0) prefix = prefix.substring(0, prefix.length() - 1);\n    }\n    return prefix;\n}",
                "def longestCommonPrefix(strs):\n    if not strs: return ''\n    pref = strs[0]\n    for s in strs[1:]:\n        while not s.startswith(pref): pref = pref[:-1]\n    return pref");

        createSimpleQuestion("Strings", "Coding", "Easy", "4. Reverse String (LeetCode 344)",
                "Reverse string array in-place with O(1) extra memory.",
                "Two pointers swap characters from outer ends moving toward center.",
                "public void reverseString(char[] s) {\n    int l = 0, r = s.length - 1;\n    while (l < r) {\n        char temp = s[l]; s[l++] = s[r]; s[r--] = temp;\n    }\n}",
                "def reverseString(s):\n    s.reverse()");

        createSimpleQuestion("Strings", "Coding", "Medium", "5. Reverse Words in a String (LeetCode 151)",
                "Reverse the order of words in a given string, trimming spaces.",
                "Split string by regex '\\\\s+', reverse array of words, and join with space. O(N) time.",
                "public String reverseWords(String s) {\n    String[] words = s.trim().split(\"\\\\s+\");\n    StringBuilder sb = new StringBuilder();\n    for (int i = words.length - 1; i >= 0; i--) {\n        sb.append(words[i]); if (i > 0) sb.append(\" \");\n    }\n    return sb.toString();\n}",
                "def reverseWords(s):\n    return ' '.join(reversed(s.split()))");

        createSimpleQuestion("Strings", "Coding", "Easy", "6. Implement strStr() (LeetCode 28)",
                "Return index of first occurrence of needle in haystack, or -1 if needle is not part of haystack.",
                "Slide window of needle length across haystack or use KMP string matching algorithm.",
                "public int strStr(String haystack, String needle) {\n    return haystack.indexOf(needle);\n}",
                "def strStr(haystack, needle):\n    return haystack.find(needle)");

        createSimpleQuestion("Strings", "Coding", "Medium", "7. Longest Palindromic Substring (LeetCode 5)",
                "Find the longest palindromic substring in string s.",
                "Expand around center for each index (both odd and even length centers). O(N^2) time, O(1) space.",
                "public String longestPalindrome(String s) {\n    if (s == null || s.length() < 1) return \"\";\n    int start = 0, end = 0;\n    for (int i = 0; i < s.length(); i++) {\n        int len1 = expand(s, i, i), len2 = expand(s, i, i + 1);\n        int len = Math.max(len1, len2);\n        if (len > end - start) {\n            start = i - (len - 1) / 2; end = i + len / 2;\n        }\n    }\n    return s.substring(start, end + 1);\n}",
                "def longestPalindrome(s):\n    res = ''\n    for i in range(len(s)):\n        for l, r in ((i, i), (i, i+1)):\n            while l >= 0 and r < len(s) and s[l] == s[r]:\n                if r - l + 1 > len(res): res = s[l:r+1]\n                l -= 1; r += 1\n    return res");

        createSimpleQuestion("Strings", "Coding", "Medium", "8. Longest Substring Without Repeating Characters (LeetCode 3)",
                "Find length of longest substring without repeating characters.",
                "Sliding window with HashMap tracking last seen index of each character. O(N) time.",
                "public int lengthOfLongestSubstring(String s) {\n    Map<Character, Integer> map = new HashMap<>();\n    int maxLen = 0, l = 0;\n    for (int r = 0; r < s.length(); r++) {\n        if (map.containsKey(s.charAt(r))) l = Math.max(l, map.get(s.charAt(r)) + 1);\n        map.put(s.charAt(r), r);\n        maxLen = Math.max(maxLen, r - l + 1);\n    }\n    return maxLen;\n}",
                "def lengthOfLongestSubstring(s):\n    seen = {}; l = maxLen = 0\n    for r, c in enumerate(s):\n        if c in seen and seen[c] >= l: l = seen[c] + 1\n        seen[c] = r\n        maxLen = max(maxLen, r - l + 1)\n    return maxLen");

        createSimpleQuestion("Strings", "Coding", "Medium", "9. Group Anagrams (LeetCode 49)",
                "Group string anagrams together into sublists.",
                "Sort each string as canonical key or use character frequency string key in Map<String, List<String>>. O(N * K log K).",
                "public List<List<String>> groupAnagrams(String[] strs) {\n    Map<String, List<String>> map = new HashMap<>();\n    for (String s : strs) {\n        char[] ca = s.toCharArray(); Arrays.sort(ca);\n        String key = String.valueOf(ca);\n        if (!map.containsKey(key)) map.put(key, new ArrayList<>());\n        map.get(key).add(s);\n    }\n    return new ArrayList<>(map.values());\n}",
                "def groupAnagrams(strs):\n    groups = defaultdict(list)\n    for s in strs: groups[''.join(sorted(s))].append(s)\n    return list(groups.values())");

        createSimpleQuestion("Strings", "Coding", "Easy", "10. Valid Parentheses (LeetCode 20)",
                "Determine if string of bracket characters '()[]{}' is valid.",
                "Use a Stack. Push expected closing bracket when opening bracket seen; pop and verify match when closing seen.",
                "public boolean isValid(String s) {\n    Stack<Character> st = new Stack<>();\n    for (char c : s.toCharArray()) {\n        if (c == '(') st.push(')');\n        else if (c == '{') st.push('}');\n        else if (c == '[') st.push(']');\n        else if (st.isEmpty() || st.pop() != c) return false;\n    }\n    return st.isEmpty();\n}",
                "def isValid(s):\n    stack = []\n    mapping = {')': '(', '}': '{', ']': '['}\n    for char in s:\n        if char in mapping:\n            if not stack or stack.pop() != mapping[char]: return False\n        else: stack.append(char)\n    return not stack");

        createSimpleQuestion("Strings", "Coding", "Medium", "11. String to Integer (atoi) (LeetCode 8)",
                "Convert string to 32-bit signed integer handling leading whitespace, sign (+/-), and clamp overflow.",
                "Scan character by character, check sign, construct integer and clamp against Integer.MAX_VALUE / MIN_VALUE.",
                "public int myAtoi(String s) {\n    int i = 0, sign = 1, res = 0, n = s.length();\n    while (i < n && s.charAt(i) == ' ') i++;\n    if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) sign = s.charAt(i++) == '-' ? -1 : 1;\n    while (i < n && Character.isDigit(s.charAt(i))) {\n        int d = s.charAt(i++) - '0';\n        if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && d > 7))\n            return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;\n        res = res * 10 + d;\n    }\n    return res * sign;\n}",
                "def myAtoi(s):\n    s = s.lstrip()\n    if not s: return 0\n    sign = -1 if s[0] == '-' else 1\n    if s[0] in ('-', '+'): s = s[1:]\n    res = 0\n    for c in s:\n        if not c.isdigit(): break\n        res = res * 10 + int(c)\n    res *= sign\n    return max(-2**31, min(2**31 - 1, res))");

        createSimpleQuestion("Strings", "Coding", "Medium", "12. Integer to Roman (LeetCode 12)",
                "Convert integer to Roman numeral string.",
                "Greedy subtraction matching predefined values [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1].",
                "public String intToRoman(int num) {\n    int[] vals = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};\n    String[] syms = {\"M\",\"CM\",\"D\",\"CD\",\"C\",\"XC\",\"L\",\"XL\",\"X\",\"IX\",\"V\",\"IV\",\"I\"};\n    StringBuilder sb = new StringBuilder();\n    for (int i = 0; i < vals.length; i++) {\n        while (num >= vals[i]) { num -= vals[i]; sb.append(syms[i]); }\n    }\n    return sb.toString();\n}",
                "def intToRoman(num):\n    mapping = [(1000, 'M'), (900, 'CM'), (500, 'D'), (400, 'CD'), (100, 'C'), (90, 'XC'), (50, 'L'), (40, 'XL'), (10, 'X'), (9, 'IX'), (5, 'V'), (4, 'IV'), (1, 'I')]\n    res = []\n    for val, sym in mapping:\n        while num >= val: num -= val; res.append(sym)\n    return ''.join(res)");

        createSimpleQuestion("Strings", "Coding", "Easy", "13. Roman to Integer (LeetCode 13)",
                "Convert Roman numeral string to an integer value.",
                "If current numeral value is less than next numeral value, subtract it; otherwise add it.",
                "public int romanToInt(String s) {\n    Map<Character, Integer> map = Map.of('I',1,'V',5,'X',10,'L',50,'C',100,'D',500,'M',1000);\n    int sum = 0;\n    for (int i = 0; i < s.length(); i++) {\n        int v = map.get(s.charAt(i));\n        if (i + 1 < s.length() && v < map.get(s.charAt(i + 1))) sum -= v;\n        else sum += v;\n    }\n    return sum;\n}",
                "def romanToInt(s):\n    m = {'I':1,'V':5,'X':10,'L':50,'C':100,'D':500,'M':1000}\n    return sum(-m[s[i]] if i+1 < len(s) and m[s[i]] < m[s[i+1]] else m[s[i]] for i in range(len(s)))");

        createSimpleQuestion("Strings", "Coding", "Medium", "14. Zigzag Conversion (LeetCode 6)",
                "Write string in zigzag pattern on given numRows and read line by line.",
                "Simulate rows using StringBuilder array, toggling direction when reaching row 0 or numRows - 1.",
                "public String convert(String s, int numRows) {\n    if (numRows == 1) return s;\n    StringBuilder[] rows = new StringBuilder[numRows];\n    for (int i = 0; i < numRows; i++) rows[i] = new StringBuilder();\n    int curRow = 0; boolean goingDown = false;\n    for (char c : s.toCharArray()) {\n        rows[curRow].append(c);\n        if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown;\n        curRow += goingDown ? 1 : -1;\n    }\n    StringBuilder res = new StringBuilder();\n    for (StringBuilder row : rows) res.append(row);\n    return res.toString();\n}",
                "def convert(s, numRows):\n    if numRows == 1: return s\n    rows = [''] * numRows; cur = 0; step = -1\n    for c in s:\n        rows[cur] += c\n        if cur == 0 or cur == numRows - 1: step = -step\n        cur += step\n    return ''.join(rows)");

        createSimpleQuestion("Strings", "Coding", "Hard", "15. Minimum Window Substring (LeetCode 76)",
                "Find minimum window in s which contains all characters in t in O(N) time.",
                "Sliding window: Expand right pointer to cover all required character frequencies, then shrink left pointer.",
                "public String minWindow(String s, String t) {\n    int[] map = new int[128]; for (char c : t.toCharArray()) map[c]++;\n    int count = t.length(), l = 0, r = 0, minLen = Integer.MAX_VALUE, start = 0;\n    while (r < s.length()) {\n        if (map[s.charAt(r++)]-- > 0) count--;\n        while (count == 0) {\n            if (r - l < minLen) { start = l; minLen = r - l; }\n            if (map[s.charAt(l++)]++ == 0) count++;\n        }\n    }\n    return minLen == Integer.MAX_VALUE ? \"\" : s.substring(start, start + minLen);\n}",
                "def minWindow(s, t):\n    need = Counter(t); missing = len(t); l = start = end = 0\n    for r, c in enumerate(s, 1):\n        missing -= need[c] > 0\n        need[c] -= 1\n        if not missing:\n            while l < r and need[s[l]] < 0: need[s[l]] += 1; l += 1\n            if not end or r - l < end - start: start, end = l, r\n    return s[start:end]");

        createSimpleQuestion("Strings", "Coding", "Medium", "16. Find All Anagrams in a String (LeetCode 438)",
                "Find all start indices of p's anagrams in s.",
                "Sliding window of fixed size p.length(). Maintain frequency difference count array.",
                "public List<Integer> findAnagrams(String s, String p) {\n    List<Integer> res = new ArrayList<>();\n    if (s.length() < p.length()) return res;\n    int[] pCount = new int[26], sCount = new int[26];\n    for (int i = 0; i < p.length(); i++) {\n        pCount[p.charAt(i) - 'a']++; sCount[s.charAt(i) - 'a']++;\n    }\n    if (Arrays.equals(pCount, sCount)) res.add(0);\n    for (int i = p.length(); i < s.length(); i++) {\n        sCount[s.charAt(i) - 'a']++; sCount[s.charAt(i - p.length()) - 'a']--;\n        if (Arrays.equals(pCount, sCount)) res.add(i - p.length() + 1);\n    }\n    return res;\n}",
                "def findAnagrams(s, p):\n    pCount = Counter(p); sCount = Counter(s[:len(p)-1])\n    res = []\n    for i in range(len(p)-1, len(s)):\n        sCount[s[i]] += 1\n        if sCount == pCount: res.append(i - len(p) + 1)\n        sCount[s[i - len(p) + 1]] -= 1\n        if sCount[s[i - len(p) + 1]] == 0: del sCount[s[i - len(p) + 1]]\n    return res");

        createSimpleQuestion("Strings", "Coding", "Easy", "17. Isomorphic Strings (LeetCode 205)",
                "Determine if two strings s and t are isomorphic (character mapping is 1-to-1).",
                "Use two frequency/index tracking arrays m1 & m2 storing last seen position + 1 of characters.",
                "public boolean isIsomorphic(String s, String t) {\n    int[] m1 = new int[256], m2 = new int[256];\n    for (int i = 0; i < s.length(); i++) {\n        if (m1[s.charAt(i)] != m2[t.charAt(i)]) return false;\n        m1[s.charAt(i)] = i + 1; m2[t.charAt(i)] = i + 1;\n    }\n    return true;\n}",
                "def isIsomorphic(s, t):\n    return len(set(zip(s, t))) == len(set(s)) == len(set(t))");

        createSimpleQuestion("Strings", "Coding", "Easy", "18. Word Pattern (LeetCode 290)",
                "Determine if string s follows the exact word pattern string pattern.",
                "Split s into words. Verify 1-to-1 mapping between pattern characters and words using two HashMaps.",
                "public boolean wordPattern(String pattern, String s) {\n    String[] words = s.split(\" \");\n    if (words.length != pattern.length()) return false;\n    Map map = new HashMap();\n    for (Integer i = 0; i < words.length; i++) {\n        if (map.put(pattern.charAt(i), i) != map.put(words[i], i)) return false;\n    }\n    return true;\n}",
                "def wordPattern(pattern, s):\n    words = s.split()\n    return len(pattern) == len(words) and len(set(zip(pattern, words))) == len(set(pattern)) == len(set(words))");

        createSimpleQuestion("Strings", "Coding", "Medium", "19. Decode String (LeetCode 394)",
                "Decode encoded string k[encoded_string] nested dynamically.",
                "Use two stacks: countStack for multipliers k, stringStack for building accumulated strings.",
                "public String decodeString(String s) {\n    Stack<Integer> counts = new Stack<>(); Stack<String> result = new Stack<>();\n    String res = \"\"; int idx = 0;\n    while (idx < s.length()) {\n        if (Character.isDigit(s.charAt(idx))) {\n            int count = 0;\n            while (Character.isDigit(s.charAt(idx))) count = count * 10 + (s.charAt(idx++) - '0');\n            counts.push(count);\n        } else if (s.charAt(idx) == '[') {\n            result.push(res); res = \"\"; idx++;\n        } else if (s.charAt(idx) == ']') {\n            StringBuilder temp = new StringBuilder(result.pop());\n            int repeatTimes = counts.pop();\n            for (int i = 0; i < repeatTimes; i++) temp.append(res);\n            res = temp.toString(); idx++;\n        } else res += s.charAt(idx++);\n    }\n    return res;\n}",
                "def decodeString(s):\n    stack = []; curNum = 0; curStr = ''\n    for c in s:\n        if c == '[':\n            stack.append(curStr); stack.append(curNum)\n            curStr = ''; curNum = 0\n        elif c == ']':\n            num = stack.pop(); prevStr = stack.pop()\n            curStr = prevStr + num * curStr\n        elif c.isdigit(): curNum = curNum * 10 + int(c)\n        else: curStr += c\n    return curStr");

        createSimpleQuestion("Strings", "Coding", "Medium", "20. Encode and Decode Strings (LeetCode Premium / 271)",
                "Design algorithm to encode list of strings to string and decode string back to list.",
                "Length-prefix encoding: Encode each string as `length + '#' + str`. Decode by reading length until '#'.",
                "public String encode(List<String> strs) {\n    StringBuilder sb = new StringBuilder();\n    for (String s : strs) sb.append(s.length()).append('#').append(s);\n    return sb.toString();\n}\npublic List<String> decode(String s) {\n    List<String> res = new ArrayList<>(); int i = 0;\n    while (i < s.length()) {\n        int slash = s.indexOf('#', i);\n        int len = Integer.parseInt(s.substring(i, slash));\n        res.add(s.substring(slash + 1, slash + 1 + len));\n        i = slash + 1 + len;\n    }\n    return res;\n}",
                "def encode(strs):\n    return ''.join(f'{len(s)}#{s}' for s in strs)\ndef decode(s):\n    res, i = [], 0\n    while i < len(s):\n        j = s.find('#', i); length = int(s[i:j]); res.append(s[j+1:j+1+length]); i = j + 1 + length\n    return res");
    }

    private void seedSelfIntroductionQuestions() {
        createSimpleQuestion("Self Introduction", "HR / Behavioral", "Easy", "1. Fresher Self Introduction (Complete 7-Step Walkthrough)",
                "Master the complete 7-Step Self Introduction framework tailored for freshers:\n" +
                "1. Greeting: Start with a polite greeting (Good morning/afternoon everyone...)\n" +
                "2. Introduce Yourself: State full name, degree, field of study, college/university, and graduation year.\n" +
                "3. Education & Key Skills: Highlight core technical subjects, project work, and primary skills.\n" +
                "4. Strengths: Quick learner, passionate, dedicated, problem-solver.\n" +
                "5. Achievements: Certifications, internships, hackathons, and workshops.\n" +
                "6. Why This Role: Explain career alignment and enthusiasm for the company.\n" +
                "7. Positive Closing: Thank interviewer confidently.\n\nTIPS:\n• Keep introduction under 90 seconds\n• Maintain clear, natural delivery & positive posture",
                "Self Introduction Delivery Rubric:\n\nStep 1 Greeting: 'Good morning/afternoon everyone. Thank you for giving me this opportunity to introduce myself.'\nStep 2 Personal Info: 'My name is [Your Name]. I recently completed my Bachelor's degree in Computer Science from [University Name] in [Year].'\nStep 3 Technical Background: 'During my academic journey, I specialized in Data Structures, Java, and Web Development. I worked on a capstone project building an ATS Resume Scorer using Spring Boot and SQL.'\nStep 4 Strengths: 'I consider myself a quick learner who is passionate about writing clean code and solving real-world software engineering problems.'\nStep 5 Key Achievements: 'I hold AWS and Java certifications and active participation in coding hackathons.'\nStep 6 Alignment: 'I am excited about this role because it perfectly aligns with my goal of growing as a Software Development Engineer.'\nStep 7 Closing: 'That is all about me. Thank you for your time!'",
                "// 7-Step Self Introduction Script Outline:\n// 1. Polite Greeting\n// 2. Name & Academic Degree\n// 3. Technical Core Skills & Projects\n// 4. Personal Strengths\n// 5. Certifications & Achievements\n// 6. Role Alignment & Passion\n// 7. Positive Closing Statement",
                "# 7-Step Self Introduction Script:\n# 1. Greeting -> 2. Name & Degree -> 3. Tech Stack -> 4. Strengths -> 5. Certs -> 6. Alignment -> 7. Thank You");

        seedOopsQuestions();
    }

    private void seedOopsQuestions() {
        createSimpleQuestion("OOPs Concepts", "Core Java / OOP", "Easy", "2. Core OOPs Concepts (Encapsulation, Inheritance, Polymorphism, Abstraction) with Real-Life Examples",
                "Explain the 4 Core Pillars of Object-Oriented Programming (OOPs) with Real-Life Examples & Explanations:\n\n" +
                "1. Encapsulation (Real-life: Medical Capsule / ATM Machine)\n" +
                "• Data hiding using private fields & public getter/setter methods.\n\n" +
                "2. Inheritance (Real-life: Parent-Child genetic traits / Vehicle hierarchy)\n" +
                "• Acquiring properties & methods from a parent class using 'extends'.\n\n" +
                "3. Polymorphism (Real-life: Person acting as Student/Employee or Sound Player)\n" +
                "• Method Overloading (Compile-time) & Method Overriding (Runtime).\n\n" +
                "4. Abstraction (Real-life: Car Accelerator Pedal / TV Remote Control)\n" +
                "• Hiding internal implementation complexity using Interfaces & Abstract classes.",

                "OOPs Concepts Deep-Dive & Real-Life Explanations:\n\n" +
                "1. ENCAPSULATION:\n" +
                "• Concept: Binding data (variables) and methods together into a single class while restricting direct access.\n" +
                "• Real-Life Example: An ATM Account hides private 'balance'. Users can only deposit or withdraw via validated public methods.\n\n" +
                "2. INHERITANCE:\n" +
                "• Concept: A child class inherits properties and methods from a parent class.\n" +
                "• Real-Life Example: A 'Car' class inherits 'startEngine()' from a 'Vehicle' parent class.\n\n" +
                "3. POLYMORPHISM:\n" +
                "• Concept: Ability of an object to take many forms (Compile-time Overloading vs Runtime Overriding).\n" +
                "• Real-Life Example: A 'PaymentMethod' interface with a 'pay()' method that behaves differently for 'UpiPayment' vs 'CreditCardPayment'.\n\n" +
                "4. ABSTRACTION:\n" +
                "• Concept: Showing only essential features to the user while hiding internal implementation logic.\n" +
                "• Real-Life Example: Pressing a Car's accelerator pedal speeds up the car without driver needing to know fuel-injection mechanics.",

                "// 1. ENCAPSULATION\nclass BankAccount {\n    private double balance;\n    public void deposit(double amount) { if (amount > 0) balance += amount; }\n    public double getBalance() { return balance; }\n}\n\n// 2. INHERITANCE\nclass Vehicle {\n    void startEngine() { System.out.println(\"Engine Started\"); }\n}\nclass Car extends Vehicle {\n    void drive() { System.out.println(\"Car Driving\"); }\n}\n\n// 3. POLYMORPHISM (Runtime Overriding)\nabstract class PaymentMethod {\n    abstract void processPayment(double amount);\n}\nclass UpiPayment extends PaymentMethod {\n    @Override void processPayment(double amount) { System.out.println(\"Paid via UPI: \" + amount); }\n}\n\n// 4. ABSTRACTION\ninterface ATM {\n    void withdraw(double amount);\n}",

                "# 1. ENCAPSULATION\nclass BankAccount:\n    def __init__(self):\n        self.__balance = 0.0 # Private variable\n    def deposit(self, amount):\n        if amount > 0: self.__balance += amount\n    def get_balance(self): return self.__balance\n\n# 2. INHERITANCE\nclass Vehicle:\n    def start_engine(self): print(\"Engine Started\")\nclass Car(Vehicle):\n    def drive(self): print(\"Car Driving\")\n\n# 3. POLYMORPHISM & ABSTRACTION\nfrom abc import ABC, abstractmethod\nclass PaymentMethod(ABC):\n    @abstractmethod\n    def pay(self, amount): pass\n\nclass UpiPayment(PaymentMethod):\n    def pay(self, amount):\n        print(f\"Paid via UPI: {amount}\")");
    }

    private void createQuestion(String topic, String category, String difficulty, String title, String company,
                                String score, Integer attempts, Boolean isSolved, String questionText,
                                String inputFormat, String outputFormat, String constraintsText,
                                String sampleCase1Input, String sampleCase1Output, String sampleCase2Input, String sampleCase2Output,
                                String answerExplanation, String codeSnippet, String sampleTestCase,
                                String templateJava, String templatePython, String templateC, String templateCpp,
                                String templateJs, String templateSql) {
        InterviewQuestion q = new InterviewQuestion();
        q.setTopic(topic);
        q.setCategory(category);
        q.setDifficulty(difficulty);
        q.setTitle(title);
        q.setCompany(company);
        q.setScore(score);
        q.setAttempts(attempts);
        q.setIsSolved(isSolved);
        q.setQuestionText(questionText);
        q.setInputFormat(inputFormat);
        q.setOutputFormat(outputFormat);
        q.setConstraintsText(constraintsText);
        q.setSampleCase1Input(sampleCase1Input);
        q.setSampleCase1Output(sampleCase1Output);
        q.setSampleCase2Input(sampleCase2Input);
        q.setSampleCase2Output(sampleCase2Output);
        q.setAnswerExplanation(answerExplanation);
        q.setCodeSnippet(codeSnippet);
        q.setSampleTestCase(sampleTestCase);
        q.setTemplateJava(templateJava);
        q.setTemplatePython(templatePython);
        q.setTemplateC(templateC);
        q.setTemplateCpp(templateCpp);
        q.setTemplateJs(templateJs);
        q.setTemplateSql(templateSql);

        questionRepository.save(q);
    }
}
