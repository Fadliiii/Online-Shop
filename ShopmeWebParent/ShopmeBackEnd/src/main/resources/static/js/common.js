	$(document).ready(function() {
		$("#logoutLink").on("click", function(e) {
			e.preventDefault();
			document.logoutForm.submit();
		});
		customizeDropDownMenu();
		customizeTabs();
	});

	function customizeDropDownMenu(){
		$(".navbar .dropdown").hover
			(function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
			},
			function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
			}
		);
		
		$(".dropdown > a").click(function(){
			location.href = this.href;
		});
	}
	
	function customizeTabs() {
	    // Function to activate the correct tab based on hash in the URL
	    function activateTabFromHash(hash) {
	        if (hash) {
	            // Select the tab link that matches the hash
	            let tabTriggerEl = document.querySelector(`.nav-tabs button[data-bs-target="#${hash}"]`);
	            if (tabTriggerEl) {
	                console.log("trigger")
	                // Create or retrieve the Bootstrap tab instance and show the tab
	                let tab = bootstrap.Tab.getOrCreateInstance(tabTriggerEl);
	                tab.show();
	            }
	        }
	    }
	 
	    // Initial activation based on URL hash when the page loads
	    const url = document.location.toString();
	    if (url.includes('#')) {
	        const hash = url.split('#')[1];
	        activateTabFromHash(hash);
	    }
	 
	    // Update the URL hash when a new tab is shown
	    document.querySelectorAll('.nav-tabs button[data-bs-toggle="tab"]').forEach(tabLink => {
	        tabLink.addEventListener('shown.bs.tab', function (e) {
	            // Update the hash in the URL without reloading the page
	            history.replaceState(null, null, e.target.hash);
	        });
	    });
		
		
		window.addEventListener('hashchange', function () {

		    const hash = window.location.hash;

		    const triggerEl = document.querySelector(
		        `.nav-tabs button[data-bs-target="${hash}"]`
		    );

		    if (triggerEl) {

		        const tab = bootstrap.Tab.getOrCreateInstance(triggerEl);

		        tab.show();
		    }
		});
		

		// User clicks tab
		document.querySelectorAll('.nav-tabs button[data-bs-toggle="tab"]')
		    .forEach(tabBtn => {

		        tabBtn.addEventListener('click', function () {

		            const target = this.getAttribute('data-bs-target');

		            window.location.hash = target;
		        });

		    });
	}
	