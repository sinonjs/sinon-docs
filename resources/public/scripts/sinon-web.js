(function () {
    // Examples
    var examples = document.getElementById("examples");

    if (!examples) {
        return;
    }

    var lis = examples.getElementsByTagName("li"), a, code, previous, previousLink;

    for (var i = 0, l = lis.length; i < l; ++i) {
        a = lis[i].getElementsByTagName("a")[0];

        a.onclick = function () {
            if (previousLink) {
                previousLink.className = "";
            }

            if (previous && previous.style) {
                previous.style.display = "none";
            }

            try {
                previous = document.getElementById(this.href.split("#")[1]);

                if (previous) {
                    previous.style.display = "block";
                }
            } catch (e) {}

            previousLink = this.parentNode;
            previousLink.className = "active";
            this.blur();
            return false;
        };

        code = document.getElementById(a.href.split("#")[1]);

        if (i != 0) {
            if (code) {
                code.style.display = "none";
            }
        } else {
            previous = code;
            previousLink = a.parentNode;
        }
    }
}());
