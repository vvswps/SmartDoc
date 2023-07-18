import os

# Define the strings to add to the HTML files
body_string = """
<div class="modal" id="delete-modal" th:if="${not #lists.isEmpty(researchFiles) or not #lists.isEmpty(bookFiles)}">
        <h2>Are you sure you want to delete this file?</h2>
        <b>This action is irrecoverable</b>
        <div>
            <button onclick="closeModal('delete-modal')">No Go back!</button>
            <a id="delete" href='/teacher/deleteFile/'>Yes DELETE</a>
        </div>
    </div>

"""
script_string = """
function showDeleteModal(id) {
            const deleteButton = document.querySelector("#delete")
            console.log(id);
            id = id.slice(id.lastIndexOf("/") + 1);
            console.log(id);

            deleteButton.setAttribute("href", "/teacher/deleteFile/" + id);
            document.querySelector("#delete-modal").classList.add('show');
        }
        """

delete_string = """<a th:href="@{'/documents/' + ${file.id}}">Download</a><button class="delete-button"
                        onclick="showDeleteModal(this.previousElementSibling.getAttribute('href'))">Delete</button>"""

# Get the current directory
dir_path = os.path.dirname(os.path.realpath(__file__))

# Loop through all HTML files in the directory
# for filename in os.listdir(dir_path):
for filename in [
    r"C:\Users\as904\git\SmartDoc\src\main\resources\templates\user\teacherFiles\awardsAchievements.html",
    r"C:\Users\as904\git\SmartDoc\src\main\resources\templates\user\teacherFiles\conferenceWorkshopSeminar.html",
    r"C:\Users\as904\git\SmartDoc\src\main\resources\templates\user\teacherFiles\fdp.html",
    r"C:\Users\as904\git\SmartDoc\src\main\resources\templates\user\teacherFiles\guestLectAndIndustrialVisits.html",
    r"C:\Users\as904\git\SmartDoc\src\main\resources\templates\user\teacherFiles\interactions.html",
]:
    if filename.endswith(".html"):
        # Open the file and read its contents
        with open(os.path.join(dir_path, filename), "r") as f:
            contents = f.read()

        # Add the body string to the body tag
        contents = contents.replace("<body>", "<body>\n" + body_string)

        # Add the script string to the script tag
        contents = contents.replace("<script>", "<script>\n" + script_string)

        contents = contents.replace(
            """<a th:href="@{'/documents/' + ${file.id}}">Download</a>""", delete_string
        )

        # Write the modified contents back to the file
        with open(os.path.join(dir_path, filename), "w") as f:
            f.write(contents)
